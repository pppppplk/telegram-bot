
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;


public class Conclusion {
    public static String getWeather(String mes) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="
                + mes + "&units=metric&appid=eec14aeb7463aebe30ab065614eee5ae"); // создаем URL и передаем mes,
        // чтобы прогноз выдавался по городу, который указан в сообщении

        URLConnection urlConnection = url.openConnection(); // предоставляем доступ к атрибутам url

        InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream()); //получение данных из  источника
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); //считывает текст
        String str = " ";

        // считываеи из url  построчно информацию и добавляем ее в строку,
        // затем возвращаем строку уже со всей информацией о погоде, которая находится в url
        while ((str = bufferedReader.readLine()) != null) {

            stringBuilder.append(str);

        }
        String response = stringBuilder.toString();
        bufferedReader.close();

        // создание json объекта
        JSONObject json = new JSONObject(response);

        float temp = json.getJSONObject("main").getFloat("temp");  // в main ищем temp
        float feels_like = json.getJSONObject("main").getFloat("feels_like"); // в main ищем feels_like
        String name = json.getString("name");

        JSONArray arr = json.getJSONArray("weather"); // массив weather, в котором находим main
        String main = null;
        for (int i = 0; i < arr.length(); i++) {
            main = arr.getJSONObject(i).getString("main");
        }

        return "*** ГОРОД " + name + " ***\n" + "Температура: " + temp + "\n"
                + "Ощущается как: " + feels_like + "\nПрогноз: " + main;

    }


}


