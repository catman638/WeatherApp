package com.bwap.weatherapp.WeatherApp.views;

import com.bwap.weatherapp.WeatherApp.controller.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Min;
import java.util.ArrayList;

@SpringUI(path = "")
public class MainView extends UI {

    @Autowired
    private WeatherService weatherService;

    private  VerticalLayout mainLyout;
    private NativeSelect<String> unitSelect;
    private TextField cityTextFeild;
    private Button searchButton;
    private HorizontalLayout dashboard;
    private Label location;
    private Label currentTemp;
    private HorizontalLayout mainDescriptionLayout;
    private Label weatherDescription;
    private Label MaxWeather;
    private Label MinWeather;
    private Label Humidity;
    private Label Pressure;
    private Label Wind;
    private Label FeelsLike;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        mainLayout();
        setHeader();
        setLogo();
        setForm();
        dashboardTitle();
        dashboardDetails();
        searchButton.addClickListener(clickEvent -> {
            if(!cityTextFeild.getValue().equals(""))
            {
                try {
                    updateUI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                Notification.show("Please enter the city name");
            }


        });
    }

    private void setForm() {
        HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);

        unitSelect = new NativeSelect<>();
        ArrayList<String> items = new ArrayList<>();
        items.add("C");
        items.add("F");

        unitSelect.setItems(items);
        unitSelect.setValue(items.get(0));
        formLayout.addComponent(unitSelect);

        mainLyout.addComponents(unitSelect);

        cityTextFeild = new TextField();
        cityTextFeild.setWidth("80%");
        formLayout.addComponent(cityTextFeild);



        //search button
        searchButton = new Button();
        searchButton.setIcon(VaadinIcons.SEARCH);
        formLayout.addComponent(searchButton);

        mainLyout.addComponents(formLayout);

    }

    private void setLogo() {
        HorizontalLayout logo = new HorizontalLayout();
        logo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Image img = new Image(null,new ClassResource("/static/logo.png"));
        logo.setWidth("240px");
        logo.setHeight("240px");

        logo.addComponent(img);
        mainLyout.addComponents(logo);
    }

    private void mainLayout() {
        mainLyout = new VerticalLayout();
        mainLyout.setWidth("100%");
        mainLyout.setSpacing(true);
        mainLyout.setMargin(true);
        mainLyout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(mainLyout);
    }

    private void setHeader(){
        HorizontalLayout header = new HorizontalLayout();
        header.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
        Label title = new Label("Weather App");

        header.addComponent(title);
        mainLyout.addComponents(header);
    }

    private void dashboardTitle()
    {
        dashboard = new HorizontalLayout();
        dashboard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        //city location
        location = new Label("currently in sonipat");
        location.addStyleName(ValoTheme.LABEL_H2);
        location.addStyleName(ValoTheme.LABEL_LIGHT);

        //current TEMP
        currentTemp = new Label("10C");
        currentTemp.setStyleName(ValoTheme.LABEL_BOLD);
        currentTemp.setStyleName(ValoTheme.LABEL_H2);

        dashboard.addComponents(location,currentTemp);

    }

    private void dashboardDetails()
    {
        mainDescriptionLayout = new HorizontalLayout();
        mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        //description layout
        VerticalLayout descriptionLayout = new VerticalLayout();
        descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        //weather description

        weatherDescription = new Label("Description: clear skies");
        weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);
        descriptionLayout.addComponents(weatherDescription);

        //Min weather
        MinWeather = new Label("Min:53");
        descriptionLayout.addComponents(MinWeather);

        //Maxweater
        MaxWeather = new Label("Max:53");
        descriptionLayout.addComponents(MaxWeather);

        VerticalLayout pressureLayout = new VerticalLayout();
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Pressure = new Label("Pressure:231Pa");
        pressureLayout.addComponents(Pressure);

        Humidity = new Label("Humidity:23");
        pressureLayout.addComponents(Humidity);

        Wind = new Label("Wind:231");
        pressureLayout.addComponents(Wind);

        FeelsLike = new Label("Feelslike:31C");
        pressureLayout.addComponents(FeelsLike);

        mainDescriptionLayout.addComponents(descriptionLayout,pressureLayout);


    }

    private void updateUI() throws JSONException {
        String city = cityTextFeild.getValue();
        String defaultUnit;
        weatherService.setCityName(city);

        if(unitSelect.getValue().equals(("F")))
        {
            weatherService.setUnit("imperials");
            unitSelect.setValue("F");
            defaultUnit = "\u00b0" + "F";
        }
        else
        {
            weatherService.setUnit("metric");
            defaultUnit = "\u00b0"+"C";
            unitSelect.setValue("C");
        }

        location.setValue("Currently in " + city);
        JSONObject mainObject = weatherService.returnMain();
        int temp = mainObject.getInt("temp");
        currentTemp.setValue(temp+defaultUnit);

        MinWeather.setValue("Min Temp : " + weatherService.returnMain().getInt("temp_min") + unitSelect.getValue());
        MaxWeather.setValue("Max Temp : " + weatherService.returnMain().getInt("temp_max") + unitSelect.getValue());
        Pressure.setValue("Pressure : " + weatherService.returnMain().getInt("pressure"));
        Humidity.setValue("Humidity : " + weatherService.returnMain().getInt("humidity"));
        Wind.setValue("Wind speed : " + weatherService.returnWind().getInt("speed"));
        FeelsLike.setValue("Feels Like : " + weatherService.returnMain().getDouble("feels_like"));

        mainLyout.addComponents(dashboard,mainDescriptionLayout);
    }
}
