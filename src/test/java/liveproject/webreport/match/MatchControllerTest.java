package liveproject.webreport.match;

import liveproject.webreport.config.TestWebConfig;
import liveproject.webreport.season.Season;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static liveproject.webreport.config.TestWebConfig.SEASON_STR;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestWebConfig.class})
@WebAppConfiguration
public class MatchControllerTest {

  private MockMvc mockMvc;

  @MockBean
  private MatchService mockService;

  @Autowired
  private MatchController controller;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void find_shouldAddMatchesToModelAndReturnOk() throws Exception {
    when(mockService.getAllBySeasonSorted(SEASON_STR)).thenReturn(Set.copyOf(Collections.singletonList(new Match())));
    mockMvc.perform(get("/matches-report/"+SEASON_STR))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void find_shouldAddSeasonToModelAndReturnOk() throws Exception {
    Season season = Season.builder()
            .teamResults(Collections.singletonList(new Season.TeamResult()))
            .build();
    when(mockService.aggregateSeason(SEASON_STR)).thenReturn(season);
    mockMvc.perform(get("/season-report/"+SEASON_STR))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void find_shouldAddEmptyMatchesToModelAndReturnNotFound() throws Exception {
    when(mockService.getAllBySeasonSorted(SEASON_STR)).thenReturn(new HashSet<>());
    mockMvc.perform(get("/matches-report/"+SEASON_STR))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json("[]"));
  }

  @Test
  public void find_shouldAddEmptySeasonToModelAndReturnNotFound() throws Exception {
    when(mockService.aggregateSeason(SEASON_STR)).thenReturn(new Season());
    mockMvc.perform(get("/season-report/"+SEASON_STR))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json("{}"));
  }

  @Test
  public void load_shouldLoadAndRenderSeasonReportView() throws Exception {
    mockMvc.perform(post("/match/"+SEASON_STR)
            .content("[]")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json("0"));
  }
}
