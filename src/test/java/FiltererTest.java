import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class FiltererTest {
    @Test
    void getBoolFilterTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Filterer filterer = new Filterer("column[1]>10 & column[5]=\"GKA\"");
        String[] values = new String[]{"3600", "\"Bowman Field\"", "\"Louisville\"", "\"United States\"", "\"LOU\"", "\"KLOU\"", "38.2280006409", "-85.6636962891", "546", "-5", "\"A\"", "\"America/New_York\"", "\"airport\"", "\"OurAirports\""};
        Method getBoolFilter = Filterer.class.getDeclaredMethod("getBoolFilter", String[].class);
        getBoolFilter.setAccessible(true);
        String boolFilter = (String) getBoolFilter.invoke(filterer, new Object[]{values});
        assertEquals("true & false", boolFilter);
    }

    @Test
    void getBoolFilterSecondTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Filterer filterer = new Filterer("(column[1]>10 & column[5]=\"GKA\") & column[1]>10 | column[5]=\"GKA\"");
        String[] values = new String[]{"3600", "\"Bowman Field\"", "\"Louisville\"", "\"United States\"", "\"LOU\"", "\"KLOU\"", "38.2280006409", "-85.6636962891", "546", "-5", "\"A\"", "\"America/New_York\"", "\"airport\"", "\"OurAirports\""};
        Method getBoolFilter = Filterer.class.getDeclaredMethod("getBoolFilter", String[].class);
        getBoolFilter.setAccessible(true);
        String boolFilter = (String) getBoolFilter.invoke(filterer, new Object[]{values});
        assertEquals("(true & false) & true | false", boolFilter);
    }

    @Test
    void matchesFilterTest() {
        Filterer filterer = new Filterer("(column[1]>10 & column[5]=\"GKA\") & column[1]>10 | column[5]=\"GKA\"");
        assertFalse(filterer.matchesFilter("3600,\"Bowman Field\",\"Louisville\",\"United States\",\"LOU\",\"KLOU\",38.2280006409,-85.6636962891,546,-5,\"A\",\"America/New_York\",\"airport\",\"OurAirports\""));
    }

    @Test
    void matchesFilterSecondTest() {
        Filterer filterer = new Filterer("(column[1]>10 & column[5]=\"GKA\") & column[1]>10 | column[5]=\"GKA\"");
        assertTrue(filterer.matchesFilter("3600,\"Bowman Field\",\"Louisville\",\"United States\",\"GKA\",\"KLOU\",38.2280006409,-85.6636962891,546,-5,\"A\",\"America/New_York\",\"airport\",\"OurAirports\""));
    }

    @Test
    void matchesFilterThirdTest() {
        Filterer filterer = new Filterer("(column[1]>10 | column[5]=\"GKA\") & column[1]>10 | column[5]=\"GKA\"");
        assertTrue(filterer.matchesFilter("3600,\"Bowman Field\",\"Louisville\",\"United States\",\"LOU\",\"KLOU\",38.2280006409,-85.6636962891,546,-5,\"A\",\"America/New_York\",\"airport\",\"OurAirports\""));
    }
}