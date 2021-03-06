package io.github.vitalivu.subdownloader.name;

import java.util.Calendar;

public class SimpleFileNameExtractor implements FileNameExtractor {
  private static final org.slf4j.Logger LOG =
      org.slf4j.LoggerFactory.getLogger(SimpleFileNameExtractor.class);

  @Override
  public Integer toYear(String fileName) {
    if (Integer.getInteger("r.release_year", -1) != -1) {
      Integer integer = Integer.getInteger("r.release_year");
      System.setProperty("r.release_year", null);
      return integer;
    }
    String[] numbers = fileName.replaceAll("[^?0-9]+", " ").trim().split("\\s+");
    for (String number : numbers) {
      if (number.trim().length() > 0) {
        int y = Integer.parseInt(number);
        if (y > 1990 && y <= Calendar.getInstance().get(Calendar.YEAR)) {
          return y;
        }
      }
    }
    return 0;
  }

  @Override
  public String toTitle(String fileName) {
    Integer year = toYear(fileName);
    String name = extractFileName(fileName, year);
    return name;
  }

  private String extractFileName(String fileName, Integer year) {
    if (fileName.startsWith("[")) {
      fileName = fileName.substring(fileName.indexOf("]") + 1);
    }
    if (year > 0 && fileName.contains(String.valueOf(year))) {
      fileName = fileName.substring(0, fileName.indexOf(String.valueOf(year)));
    }

    if (fileName.contains("1080")) {
      fileName = fileName.substring(0, fileName.lastIndexOf("1080"));
    } else if (fileName.contains("720")) {
      fileName = fileName.substring(0, fileName.lastIndexOf("720"));
    }
    return fileName
        .replaceAll("\\.", " ")
        .replaceAll(" \\(", "").trim();
  }
}
