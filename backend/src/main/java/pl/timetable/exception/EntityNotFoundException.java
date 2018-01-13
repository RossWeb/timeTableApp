package pl.timetable.exception;

public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(Integer id, String objectName) {
        super("Object : " + objectName + " by id : " + id + " not found ");
    }
}
