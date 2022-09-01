package no.sparebank1.turbo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class TurboConfigTest {

    @Test
    public void shall_not_run_when_we_are_on_buildserver() {
        assertTrue(TurboConfig.shallNotRun(TurboConfig.M2_REPOSITORY_PATH_ON_BUILDSERVER, Collections.emptyList()));
    }

    @Test
    public void shall_not_run_when_we_are_running_a_plugin() {
        List<String> goals = new ArrayList<>();
        goals.add("versions:set");
        assertTrue(TurboConfig.shallNotRun("/home/.m2/repository", goals));
    }

    @Test
    public void shall_find_paramvalue_in_xml() {
        String xml = "<enable>true</enable><turbo>false</turbo>";
        String enable = TurboConfig.xmlToStringValue(xml, "enable").get();
        assertEquals("true", enable);
    }

    @Test
    public void shall_accept_missing_param_in_xml_as_empty_optional() {
        String xml = "<enable>true</enable><turbo>false</turbo>";
        Optional nothere = TurboConfig.xmlToStringValue(xml, "nothere");
        assertTrue(nothere == Optional.empty());
    }

    @Test
    public void shall_handle_empty_configuration() {
        assertEquals("defaultvalue", TurboConfig.getParameter("", "someparam", "defaultvalue"));
    }
}