package elements;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public abstract class ElementData
{
    /**
     * the file types that XMLReader can read
     */
    public static enum FileType
    {
        CATEGORY,
        INDEX;
    }//FileType

    /**
     * the data types that XMLWriter can write
     */
    public static enum DataType
    {
        CATEGORY,
        INDEX_NOTE,
        NOTE;
    }//DataType


    private DataType dataType;
    private String createTimeInStr;

    /**
     * constructor
     * @param givenDataType the type of the data(e.g. Note)
     * @param createTime the time went the data is generated
     */
    public ElementData(DataType givenDataType)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        Calendar now = Calendar.getInstance();

        createTimeInStr = sdf.format(now.getTime());
        dataType = givenDataType;
    }//constructor

    /**
     * @return the type of the data(e.g. Note)
     */
    public DataType getDataType()
    {
        return dataType;
    }//getDataType

    /**
     * transfer the data that the object contains into a Element object
     * that can save in a category file
     * @param doc the Document object that used to read the xml file
     * @return data in Element
     */
    public abstract Element toElement(Document doc);

    /**
     * transfer the data that the object contains into a Element object
     * that can save in a index file
     * @param doc the Document object that used to read the xml file
     * @return data in Element
     */
    public abstract Element toIndexElement(Document doc);

    /**
     * @return return the time when the data is generated
     */
    public String getTime()
    {
        return createTimeInStr;
    }//getTime
}//abstract class