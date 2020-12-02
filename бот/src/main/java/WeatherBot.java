
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;



public class WeatherBot  extends TelegramLongPollingBot {


    private final String TOKEN = "1441901192:AAHU9laJ5_az06tbmI_h8psGGe2jzscpa84";


    public WeatherBot() {
        super();
    }

    public String getBotUsername() {
        return "WeatherAssistant";
    }

    public String getBotToken() {

        return TOKEN;
    }

    public static ArrayList<String> arrCity = new ArrayList<>();
    public static ArrayList<String> arrId = new ArrayList<>();

    public ArrayList<String> getArrCity(){
        return arrCity;
    }

    public ArrayList<String> getArrId(){
        return arrId;
    }

    public void setArrCity(ArrayList<String> arrCity){
        this.arrCity = arrCity;

    }

    public void setArrId(ArrayList<String> arrId){
        this.arrId = arrId;
    }


    // работа с сообщениями

    public void onUpdateReceived(Update update) {
        Message mes = update.getMessage();

        if (mes != null  && mes.hasText()) {
            long id = mes.getChatId();
            String chat_id = Long.toString(id);
            switch (mes.getText()){
                // прописываю команды бота
                case "/start":
                    try {
                        execute(new SendMessage(chat_id,"Привет, я бот - ассистент!\n" +
                                "Данный бот высылает пользователю прогноз погоды,\n" +
                                "также ты можешь подписаться на ежедневную рассылку прогноза в твоем городе ⛅!\n" +
                                "С помощью /help можно узнать о возможностях бота."));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }break;
                case "/help":
                    try {
                        execute(new SendMessage(chat_id, "*** ПОМОЩНИК ***\n" +
                                "/subscribe  -  функция подписки на расслыку прогноза погоды\n" +
                                "/unsubscribe - функция отписки от рассылки прогноза погоды\n" +
                                "Введите название города и бот выдаст вам прогноз погоды"));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }break;
                case "/subscribe":
                    try {
                        execute(new SendMessage(chat_id, "Поздравляю, вы подписались на рассылку!\n" +
                                "Введите город, прогноз погоды которого вы хотите получать"));
                        if(arrId.contains(chat_id)){ //проверяем находится ли id в массиве
                            System.out.println("уже существует");
                        }else{
                            arrId.add(chat_id);
                            arrCity.add("Moscow");
                            System.out.println(arrId);
                            System.out.println(arrCity);
                        }

                    } catch (TelegramApiException  e) {
                        e.printStackTrace();

                    }break;

                case "/unsubscribe":
                    try {
                        if(arrId.contains(chat_id)){
                            execute(new SendMessage(chat_id, "Вы отписались от рассылки"));
                            int index = arrId.indexOf(chat_id); //определяю индекс пользователя в массиве
                            arrId.remove(index); // по индексу удаляем элемент
                            arrCity.remove(index);
                        }else{
                            execute(new SendMessage(chat_id, "Вы еще не подписались"));
                        }
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }break;

                default:

                    long idUserChat = mes.getChatId(); // создаем переменную для chat id, чтобы работать в default
                    String mesGetChatId = Long.toString(idUserChat);

                    try{

                        // если чат id существует в массиве и если город существует,
                        // тогда мы добавляем город в массив
                        if(arrId.contains(mesGetChatId)){

                            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="
                                    + mes.getText() + "&units=metric&appid=eec14aeb7463aebe30ab065614eee5ae"); // создаем URL и передаем mes,
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

                            //set - менять; 1 элемент - индекс; 2 элемент - текст(то, на что меняем)
                            // тот элемент, который стоит на месте, котором находится элемент id по счету,
                            // меняется на тот город, который задает пользователь

                            arrCity.set(arrId.lastIndexOf(mesGetChatId), mes.getText()); //меняет объект города по индексу id
                            System.out.println(arrCity);


                        }else{


                            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="
                                    + mes.getText() + "&units=metric&appid=eec14aeb7463aebe30ab065614eee5ae"); // создаем URL и передаем mes,
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

                            System.out.println("не существует");

                        }

                        execute(new SendMessage(chat_id, Conclusion.getWeather(mes.getText())));
                    }catch (IOException | TelegramApiException e) {
                        try {
                            execute(new SendMessage(chat_id, "К сожалению, я не знаю такого города \uD83D\uDE14.\n Попробуйте еще раз!"));
                        } catch (TelegramApiException telegramApiException) {
                            telegramApiException.printStackTrace();
                        }
                    }break;
            }

        }


    }


    //метод для отправки сообщения для /subscribe

    private String chatID;
    private String mestext;

    public void SendMes(String chatID, String mestext){
        SendMessage message = new SendMessage().setChatId(chatID).setText(mestext); //создаем сообщение и передаем id и текс
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }


}
