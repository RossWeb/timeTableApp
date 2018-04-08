package pl.timetable.exception;

public class EntityDuplicateFoundException extends Exception {

    public EntityDuplicateFoundException(Integer id, String objectName) {
        super("Object : " + objectName + " by id : " + id + " duplicate found ");
    }
}
