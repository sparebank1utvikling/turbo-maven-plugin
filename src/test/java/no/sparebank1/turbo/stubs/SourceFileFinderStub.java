package no.sparebank1.turbo.stubs;

import no.sparebank1.turbo.SourceFileFinder;

import java.util.ArrayList;
import java.util.List;

public class SourceFileFinderStub extends SourceFileFinder {
  @Override
  public List<String> getSourceFiles(final String projectRoot, final String ignoreChangesInFiles) {
    return new ArrayList<String>();
  }
}