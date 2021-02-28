package net.notfab.lindsey.api.rest;

import net.notfab.lindsey.shared.enums.Flags;
import net.notfab.lindsey.shared.enums.Language;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnumRest {

    @GetMapping("languages")
    public Language[] getLanguages() {
        return Language.values();
    }

    @GetMapping("countries")
    public Flags[] getCountries() {
        return Flags.values();
    }

}
