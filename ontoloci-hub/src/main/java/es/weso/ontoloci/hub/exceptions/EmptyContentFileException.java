package es.weso.ontoloci.hub.exceptions;

public class EmptyContentFileException extends  RuntimeException{
    public EmptyContentFileException(){
        super("File without content");
    }
}
