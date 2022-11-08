package uk.gov.hscic;

public final class SystemVariable {

    private SystemVariable() { }

    // ***Change this for upgrade version***
    public static final String VERSION = "1.5.0";
    
    public static final String DATE_TIME_REGEX = "-?[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00)))?)?)?";
    public static final String DATE_REGEX = "-?[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])))?";
    
}
