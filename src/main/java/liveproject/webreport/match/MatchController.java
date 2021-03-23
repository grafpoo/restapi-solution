package liveproject.webreport.match;

import liveproject.webreport.season.Season;
import liveproject.webreport.season.SeasonsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("season")
    public ResponseEntity<SeasonsList> getAllSeasons() {
        List<String> seasons = matchService.getAllSeasons();
        HttpStatus status = seasons.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(status).body(new SeasonsList(seasons));
    }

    @GetMapping("season-report/{season}")
    public ResponseEntity<Season> seasonReport(@PathVariable("season") String seasonStr) {
        Season season = matchService.aggregateSeason(seasonStr);
        HttpStatus status = season.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(status).body(season);
    }

    @GetMapping("matches-report/{season}")
    public ResponseEntity<Set<Match>> matchesReport(@PathVariable("season") String season) {
        Set<Match> matches = matchService.getAllBySeasonSorted(season);
        HttpStatus status = matches.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(status).body(matches);
    }

    @PostMapping("/match/{season}")
    @ResponseStatus(value = HttpStatus.OK)
    public Integer addMatches(@PathVariable String season, @RequestBody List<Match> matches) {
        Map<String, Integer> counts = matchService.saveAll(season, matches);
        int totalCount = counts.values().stream()
                .mapToInt(i -> i) // this unboxes
                .sum();
        return totalCount;
    }
}
