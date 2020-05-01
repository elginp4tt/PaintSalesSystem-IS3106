package jsf.converter;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;



@FacesConverter(value = "dateConverter", forClass = Date.class)

public class DateConverter implements Converter 
{
    
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {

        if (value == null || value.length() == 0 || value.equals("null")) 
        {
            return null;
        }

        try
        {            
            if(value.length() < 9)
            {
                value += " 00:00:00";
            }
            
            return dateFormat.parse(value);
        }
        catch(ParseException ex)
        {
            throw new IllegalArgumentException("Invalid date");
        }
    }

    
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        
        if (value == null) 
        {
            return "";
        }
        
        if (value instanceof String)
        {
            return "";
        }
        
        
        if (value instanceof Date)
        {            
            Date date = (Date) value;                        
            
            try
            {
                return dateFormat.format(date);
            }
            catch(Exception ex)
            {
                throw new IllegalArgumentException("Invalid value");
            }
        }
        else 
        {
            throw new IllegalArgumentException("Invalid value");
        }
    }
}
