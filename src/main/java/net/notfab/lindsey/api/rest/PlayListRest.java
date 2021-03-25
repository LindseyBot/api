package net.notfab.lindsey.api.rest;

import net.notfab.lindsey.api.advice.paging.KeySetPaginator;
import net.notfab.lindsey.api.advice.paging.PagedResponse;
import net.notfab.lindsey.api.advice.security.SessionProvider;
import net.notfab.lindsey.api.models.*;
import net.notfab.lindsey.shared.entities.music.Track;
import net.notfab.lindsey.shared.entities.playlist.PlayList;
import net.notfab.lindsey.shared.entities.playlist.PlayListVote;
import net.notfab.lindsey.shared.entities.profile.UserProfile;
import net.notfab.lindsey.shared.repositories.sql.PlayListRepository;
import net.notfab.lindsey.shared.repositories.sql.PlayListVoteRepository;
import net.notfab.lindsey.shared.repositories.sql.UserProfileRepository;
import net.notfab.lindsey.shared.services.PlayListService;
import net.notfab.lindsey.shared.utils.Snowflake;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("playlists")
public class PlayListRest {

    private final Snowflake snowflake;
    private final SessionProvider session;
    private final PlayListRepository repository;
    private final PlayListService service;
    private final UserProfileRepository profiles;
    private final PlayListVoteRepository voteRepository;

    public PlayListRest(Snowflake snowflake, SessionProvider session, PlayListRepository repository,
                        PlayListService service, UserProfileRepository profiles, PlayListVoteRepository voteRepository) {
        this.snowflake = snowflake;
        this.session = session;
        this.repository = repository;
        this.service = service;
        this.profiles = profiles;
        this.voteRepository = voteRepository;
    }

    @GetMapping
    public List<FilledPlayListSummary> discover(KeySetPaginator paginator) {
        return this.voteRepository.findMostVoted(paginator.getCursor(), paginator.getLimit()).stream()
                .map(item -> {
                    FilledPlayListSummary filled = new FilledPlayListSummary(item);
                    filled.setSongs(this.service.size(item.getId()));
                    return filled;
                }).collect(Collectors.toList());
    }

    @GetMapping("@me")
    public PagedResponse<PlayList> getSelf() {
        return PagedResponse.ofUnpaged(repository.findAllByOwner(session.getUser().getId()));
    }

    @GetMapping("{id:[0-9]+}")
    public FakePlayList findById(@PathVariable("id") long id) {
        Optional<PlayList> oPlayList = repository.findById(id);
        if (oPlayList.isEmpty()) {
            throw new IllegalArgumentException("Unknown playlist");
        }
        PlayList playList = oPlayList.get();
        if (!service.canRead(playList, session.getUser().getId())) {
            throw new AccessDeniedException("No permission");
        }
        DiscordUser user = new DiscordUser();
        user.setId(playList.getOwner());
        Optional<UserProfile> oProfile = this.profiles.findById(playList.getOwner());
        if (oProfile.isPresent()) {
            UserProfile profile = oProfile.get();
            user.setUsername(profile.getName());
        } else {
            user.setUsername("Anonymous#000");
        }
        return new FakePlayList(playList, user);
    }

    @GetMapping("{id:[0-9]+}/tracks")
    public List<Track> getTracks(@PathVariable("id") long id) {
        Optional<PlayList> oPlayList = repository.findById(id);
        if (oPlayList.isEmpty()) {
            throw new IllegalArgumentException("Unknown playlist");
        }
        PlayList playList = oPlayList.get();
        if (!service.canRead(playList, session.getUser().getId())) {
            throw new AccessDeniedException("No permission");
        }
        return this.service.getAllTracks(id);
    }

    @GetMapping("{id:[0-9]+}/votes")
    public VoteSummary getVotes(@PathVariable long id) {
        Optional<PlayList> oPlayList = repository.findById(id);
        if (oPlayList.isEmpty()) {
            throw new IllegalArgumentException("Unknown playlist");
        }
        VoteSummary summary = new VoteSummary();
        summary.setUp(this.voteRepository.countByPlaylistAndUpvote(id, true));
        summary.setDown(this.voteRepository.countByPlaylistAndUpvote(id, false));
        Optional<PlayListVote> vote = this.voteRepository.findByUserAndPlaylist(this.session.getUser().getId(), id);
        vote.ifPresent(playListVote -> summary.setVoted(playListVote.isUpvote()));
        return summary;
    }

    @PutMapping("{id:[0-9]+}/votes")
    public VoteSummary vote(@PathVariable long id, @RequestBody VoteRequest request) {
        Optional<PlayList> oPlayList = repository.findById(id);
        if (oPlayList.isEmpty()) {
            throw new IllegalArgumentException("Unknown playlist");
        }
        Optional<PlayListVote> oVote = this.voteRepository
                .findByUserAndPlaylist(this.session.getUser().getId(), id);
        if (request.getVote() == null) {
            oVote.ifPresent(this.voteRepository::delete);
            return this.getVotes(id);
        }
        PlayListVote vote;
        if (oVote.isEmpty()) {
            vote = new PlayListVote();
            vote.setId(this.snowflake.next());
            vote.setPlaylist(id);
            vote.setUser(this.session.getUser().getId());
        } else {
            vote = oVote.get();
        }
        vote.setUpvote(request.getVote());
        this.voteRepository.save(vote);
        return this.getVotes(id);
    }

}
