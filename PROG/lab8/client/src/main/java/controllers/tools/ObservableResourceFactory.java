package controllers.tools;


import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Observable Resource Factory.
 */
public class ObservableResourceFactory {

    private ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();

    /**
     * @return Resources.
     */
    public ObjectProperty<ResourceBundle> resourcesProperty() {
        return resources;
    }

    /**
     * @return Resource bundle.
     */
    public final ResourceBundle getResources() {
        return resourcesProperty().get();
    }

    /**
     * Set resources.
     *
     * @param resources Resources.
     */
    public final void setResources(ResourceBundle resources) {
        resourcesProperty().set(resources);
    }

    /**
     * Binds strings.
     *
     * @param key Key for resource.
     * @return Binding string.
     */
    public StringBinding getStringBinding(String key) {
        return new StringBinding() {
            {
                bind(resourcesProperty());
            }

            @Override
            public String computeValue() {
                return getResources().getString(key);
            }
        };
    }

    public StringBinding getFormattedBunding(String key) {
        return new StringBinding() {
            {
                bind(resourcesProperty());
            }

            @Override
            public String computeValue() {
                return getString(key);
            }
        };
    }
    public String getString(String key){
        String res;
        if(key.contains(" ") && key.contains("[") && key.contains("]")){
            String[] seq = key.split(" ");
            List<String> list = new LinkedList<>();
            for(String e: seq){
                if(e.length()>=3&& e.charAt(0)=='[' && e.charAt(e.length()-1)==']'){
                    list.add(e.substring(1,e.length()-1));
                }
            }
            if(list.size()==0) throw new ResourceException(key);
            key = list.get(0);
            list.remove(0);

            Object[] args = list.toArray();
            res = MessageFormat.format(getRawString(key), args);
        }
        else if(key.length()>=3&& key.charAt(0)=='[' && key.charAt(key.length()-1)==']'){
            res = getRawString(key.substring(1,key.length()-1));
        }else{
            throw new ResourceException(key);
        }

        return res;
    }
    public String getRawString(String key){
        if(!getResources().containsKey(key)) throw  new ResourceException(key);
        return getResources().getString(key);
    }

}
