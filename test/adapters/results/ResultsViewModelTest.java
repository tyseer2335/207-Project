package adapters.results;

import main.adapters.results.ResultsState;
import main.adapters.results.ResultsViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultsViewModelTest {
    private ResultsViewModel resultsViewModel;
    private ResultsState testResultsState;
    private final String dummyResults = "some results here";
    private final String dummyError = "some error here";

    @BeforeEach
    void setUp() {
        this.resultsViewModel = new ResultsViewModel();
    }

    @Test
    void getState() {
        String expectedResults = "";
        assertEquals(expectedResults, resultsViewModel.getState().getResults());
        assertNull(resultsViewModel.getState().getError());
    }

    @Test
    void setState() {
        this.testResultsState = new ResultsState();
        testResultsState.setError(dummyError);
        testResultsState.setResults(dummyResults);
        resultsViewModel.setState(testResultsState);
        assertEquals(dummyResults, resultsViewModel.getState().getResults());
        assertEquals(dummyError, resultsViewModel.getState().getError());
    }
}