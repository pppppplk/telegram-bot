import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, TelegramApiException {


        ApiContextInitializer.init(); // инициализируем API

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();



        WeatherBot arrCityBot = new WeatherBot();
        ArrayList<String> arrCity = arrCityBot.getArrCity();
        arrCityBot.setArrCity(arrCity);

        WeatherBot arrIDBot = new WeatherBot();
        ArrayList<String> arrID = arrIDBot.getArrId();
        arrIDBot.setArrId(arrID);

        WeatherBot sm = new WeatherBot(); //создаем переменую для отправки сообщения для /subscride

        try {
            telegramBotsApi.registerBot(new WeatherBot()); // регистрируем API
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        while (true){

            arrCity = arrCityBot.getArrCity();
            arrID = arrIDBot.getArrId();
            arrIDBot.setArrId(arrID);
            arrCityBot.setArrCity(arrCity);
            System.out.println("из main 1" +  arrCity + "\n" + "ID: "+arrID); //вывод массива id и массива городов


            if(arrCity.size() > 0){ // если размер массива > 0, тогда выполняю действие ниже

                for(int i =0; i<arrCity.size() ; i++){


                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="
                            + arrCity.get(i) + "&units=metric&appid=eec14aeb7463aebe30ab065614eee5ae");

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

                    System.out.println(response);




                    // создание json объекта
                    JSONObject json = new JSONObject(response);

                    float temp = json.getJSONObject("main").getFloat("temp");  // в main ищем temp
                    float feels_like = json.getJSONObject("main").getFloat("feels_like"); // в main ищем feels_like
                    String name = json.getString("name");

                    JSONArray arr = json.getJSONArray("weather"); // массив weather, в котором находим main
                    String main = null;
                    for (int v = 0; v < arr.length(); v++) {
                        main = arr.getJSONObject(v).getString("main");
                    }

                    String t= "*** ГОРОД " + name + " ***\n" + "Температура: " + temp + "\n"
                            + "Ощущается как: " + feels_like + "\nПрогноз: " + main;

                    sm.execute(new SendMessage(arrID.get(i), t)); // отправка сообщения от бота при /subscribe

                }

            }

            TimeUnit.SECONDS.sleep(10);

            //TimeUnit.MINUTES.sleep(1);

            //Thread.sleep(10000); // 10.000- 10 секунд

        }

    }


}
