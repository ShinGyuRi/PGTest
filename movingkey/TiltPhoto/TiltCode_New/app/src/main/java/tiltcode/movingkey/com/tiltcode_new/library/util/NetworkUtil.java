package tiltcode.movingkey.com.tiltcode_new.library.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import retrofit.Endpoint;
import retrofit.RestAdapter;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import tiltcode.movingkey.com.tiltcode_new.Model.HttpService;
import tiltcode.movingkey.com.tiltcode_new.R;
import tiltcode.movingkey.com.tiltcode_new.library.BaseApplication;

/**
 * Created by Gyul on 2016-06-20.
 */
public class NetworkUtil {

    public static String TAG = NetworkUtil.class.getSimpleName();

    public static Context context = BaseApplication.getContext();

    public static UrlEndPoint endPoint;

    //앱내에서 모든 http 통신은 httpservice로 동작
    private static HttpService httpService;

    //HttpService의 EndPoint
    public static class UrlEndPoint implements Endpoint {
        private static final String BASE = context.getResources().getString(R.string.API_SERVER);

        private String url = BASE;

        public void setPort(String port) {
            url = BASE +":"+ port;
        }

        @Override
        public String getName() {
            return "default";
        }

        @Override
        public String getUrl() {
            Log.d(TAG,"url : "+url);
            if (url == null) setPort("80");
            return url;
        }
    }

    //Singleton Endpoint
    public static UrlEndPoint getEndPoint(){
        if(endPoint==null){
            endPoint = new UrlEndPoint();
        }
        return endPoint;
    }

    //Singleton HttpService
    public static HttpService getHttpSerivce() {

        if(httpService==null) {

            RestAdapter restAdapter =
                    new RestAdapter.Builder()
                            .setEndpoint(getEndPoint().getUrl())
                            .setConverter(new DynamicJsonConverter())
                            .build();
            httpService = restAdapter.create(HttpService.class);
        }
        return httpService;
    }

    static class DynamicJsonConverter implements Converter {

        @Override public Object fromBody(TypedInput typedInput, Type type) throws ConversionException {
            try {
                InputStream in = typedInput.in(); // convert the typedInput to String
                String string = fromStream(in);
                in.close(); // we are responsible to close the InputStream after use

                if (String.class.equals(type)) {
                    return string;
                } else {
                    return new Gson().fromJson(string, type); // convert to the supplied type, typically Object, JsonObject or Map<String, Object>
                }
            } catch (Exception e) { // a lot may happen here, whatever happens
                throw new ConversionException(e); // wrap it into ConversionException so retrofit can process it
            }
        }

        @Override public TypedOutput toBody(Object object) { // not required
            return null;
        }

        private static String fromStream(InputStream in) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append("\r\n");
            }
            return out.toString();
        }
    }

}
