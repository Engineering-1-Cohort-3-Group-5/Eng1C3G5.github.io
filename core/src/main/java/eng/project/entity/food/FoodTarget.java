package eng.project.entity.food;

/** A food-search result; squared distance avoids an unnecessary square-root calculation. */
public record FoodTarget(Food food, float distance2) {
}
