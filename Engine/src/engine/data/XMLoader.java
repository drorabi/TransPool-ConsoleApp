package engine.data;

import engine.exceptions.FileDoesNotExist;
import engine.exceptions.NotXmlFile;

import javax.xml.bind.JAXBException;

public interface XMLoader {

    void loadXML() throws NotXmlFile, FileDoesNotExist, JAXBException;
}
