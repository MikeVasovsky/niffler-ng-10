package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RandomDataUtils {

  private RandomDataUtils() {
  }

  private static final Faker faker = new Faker();

  public static String randomUsername() {
    return faker.name().username();
  }

  public static String randomName() {
    return faker.name().firstName();
  }

  public static String randomSurname() {
    return faker.name().lastName();
  }

  public static String randomCategoryName() {
    return faker.food().fruit();
  }

   public static String randomSentence(int wordsCount) {
    return faker.lorem().sentence(wordsCount);
  }
}
