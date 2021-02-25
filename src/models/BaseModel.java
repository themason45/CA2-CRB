package models;

/**
 * Defines a pk that is universal for all models, this allows it to be looked up in an ArrayList easily, as well as
 * other things, which can be found in {@link support.ModelWrapper}.
 */
public class BaseModel {
    public int pk;

    public BaseModel(int pk) {
        this.pk = pk;
    }
}
