package sample;


import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;


public class Controller implements Initializable {

    //###### NOTE: ####### FUNCTIONALITY and VARIABLES have been named as per "customer's/clients requirements" (Functionality refers to assignments Question 1, Question 2, Question 3)
    //############## Question1 (Q1) - Refers to the Application's Tab "Last Year Stats and Report"
    //############## Question2 (Q2) - Refers to the Application's Tab "Year Comparison"
    //############## Question3 (Q3) - Refers to the Application's Tab "Station Comparison and Report"


    // The references, which are injected by the FXML loader

    @FXML
    private Button Q1TXTReport; //Question 1 text report button (Report_1.txt)

    @FXML
    private Button Q1CSVReport; //Question 1 csv report button (Report_1.csv)

    @FXML
    private Button Q3clearData; //Button to clear data for Question 3

    @FXML
    private BarChart Q3AFChart; //Question 3 Barchart for Average Annual air frost days.

    @FXML
    private BarChart Q3RainfallChart; //Question 3 Barchart for Average Annual Rainfall Chart.

    @FXML
    private Button generateReportCSV; // Question3 generate Report CSV Button (Report_2.csv).

    @FXML
    private Button generateReport; //Question 3 generate Report Text Button (Report_2.txt).

    @FXML
    private Label selectedYear1; //Question 2 Selected Year 1, Label shows which year was selected from dropdown.

    @FXML
    private Label selectedYear2; //Question 2 Selected Year 2 (Compare to year), Label shows which year was selected from dropdown.

    @FXML
    private Label Q2SelectedStation; //Question 2 Label shows, which Station has been selected from the dropdown.

    @FXML
    private LineChart AFChart; //Question 2 LineChart for Air Frost Days

    @FXML
    private BarChart tMaxChart; //Question 2 BarChart for The Mean Max Temperature.

    @FXML
    private BarChart tMinChart; //Question 2 BarChart for The Mean Min Temperature.

    @FXML
    private Button Q2clearData; //Question 2 Clear Data button

    @FXML
    private MenuButton question1menubar; //Question1 Dropdown menu with station names.

    @FXML
    private MenuButton question2menubar; //Question 2 Dropdown menu with Station names.

    @FXML
    private MenuButton question3menubar; //Question 3 Dropdown menu with Station names.

    @FXML
    private MenuButton chartYearCompare; //Question 2 Dropdown menu with Year 2 (Comparison years).

    @FXML
    private MenuButton chartYear; //Question 2 Dropdown menu with Year 1 years.

    @FXML
    private LineChart RainfallChart; //Question 2 LineChart for Rainfall / month.

    @FXML
    private Label selectedStation; //Question 1 Label for Selected Station Name.

    @FXML
    private Label tMax; //Question 1 Label for tMax figures.

    @FXML
    private Label tMin; //Question 1 Label for tMin figures.

    @FXML
    private Label AF;//Question 1 Label for Air Frost Days figures.

    @FXML
    private Label Rainfall;//Question 1 Label for Rainfall figures.




    //public no-args constructor
    public Controller() {
    }




//CONTROLLER CLASS VARIABLES:

    List<String> csvNames = new ArrayList<>(); //Holds all dynamically extracted station names from .csv files.

    String year = "2011"; //placeholder, Question 2 will be initially set on year 2011.
    String year2 = ""; //User for comparison year Question 2, initially empty.
    String StationName = ""; //Used for Question 1
    String StationName2 = ""; //Used for Question 2
    String StationName3 = ""; //Used for Question 3


    //Initially collected and calculated Station CSV data (Strings)
    List<String> inputMax = new ArrayList<>();
    List<String> inputMin = new ArrayList<>();
    List<String> inputAF = new ArrayList<>();
    List<String> inputRainfall = new ArrayList<>();

    //Converted to doubles CSV Station data.
    double[] convertedMax = new double[10]; //the size is just a placeholder.
    double[] convertedMin = new double[10];
    double[] convertedTotalAF = new double[10];
    double[] convertedTotalRainfall = new double[10];

    List<String> q2CompareYears = new ArrayList<>(); //Question 2 List, collecting all selected Comparison years, to be displayed.

    //FOR QUESTION 3
    List<String> years = new ArrayList<>(); //Holds ALL Station Years/Months from Station CSV files.
    List<String> months = new ArrayList<>();
    int monthsInaYear = 12; //Used for Q3, when counting averages

    //Q3 REPORT VARIABLES
    String tMaxMonth ="";
    String tMaxYear="";
    String tMinMonth="";
    String tMinYear="";
    double averageAnnualAF=0.0;
    double averageAnnualRainfall=0.0;

    //Q3 VISUALISATION VARIABLES
    XYChart.Series seriesAnnualRainfall = new XYChart.Series(); // ANNUAL RAINFALL SERIES
    XYChart.Series seriesAnnualAF = new XYChart.Series(); // ANNUAL AF SERIES

    //Q1 REPORT VARIABLES
    double theMax = 0.0;
    double theMin = 0.0;
    int castedAF =0;
    double totalRainfall =0.0;


//########################################### ALL REPETITIVE APPLICATION'S FUNCTIONS (Methods) [START HERE] #########################################

    //csvReader - parameters take a year and station's name, dynamically reads any file ending with ".csv" inside the specified path,
    //extracts tMax,tMin,Air frost days and Rainfall data, according to the provided year(or all years) and station's name,
    //converts the data to doubles, stores it in designated Class Variable's.

    private void csvReader(String year, String StationName) {
        try {
            inputMax.clear();
            inputMin.clear();
            inputAF.clear();
            inputRainfall.clear();
            years.clear();
            months.clear();

            //The file will be read dynamically form "csv" folder, getting its path and Station's name ending with ".csv".
            File inputFile = new File(getClass().getResource("/csv/").getPath() + StationName + ".csv");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].contains(year) || year.contains("all")) { //if "all" is passed, data for all years will be collected in .csv
                    inputMax.add(data[2]);
                    inputMin.add(data[3]);
                    inputAF.add(data[4]);
                    inputRainfall.add(data[5]);
                    years.add(data[0]);
                    months.add(data[1]);
                }
            }
            //IF at least one of the lists is empty then do not compute the below:
            if (!inputMax.isEmpty() & !inputMin.isEmpty() & !inputAF.isEmpty() & !inputRainfall.isEmpty()) {

                //CONVERTED T MAX VALUES IN AN ARRAY
                this.convertedMax = new double[inputMax.size()];

                for (int i = 0; i < inputMax.size(); ++i) { //iterate over the elements of the list
                    this.convertedMax[i] = Double.parseDouble(inputMax.get(i)); //store each element as a double in the array
                }


                //CONVERTED T MIN VALUES IN AN ARRAY
                this.convertedMin = new double[inputMin.size()];
                for (int i = 0; i < inputMin.size(); ++i) { //iterate over the elements of the list
                    this.convertedMin[i] = Double.parseDouble(inputMin.get(i)); //store each element as a double in the array
                }

                //CONVERTED AF VALUES IN AN ARRAY
                this.convertedTotalAF = new double[inputAF.size()];
                for (int i = 0; i < inputAF.size(); ++i) { //iterate over the elements of the list
                    this.convertedTotalAF[i] = Double.parseDouble(inputAF.get(i)); //store each element as a double in the array
                }

                //CONVERTED RAINFALL VALUES IN AN ARRAY
                this.convertedTotalRainfall = new double[inputRainfall.size()];
                for (int i = 0; i < inputRainfall.size(); ++i) { //iterate over the elements of the list
                    this.convertedTotalRainfall[i] = Double.parseDouble(inputRainfall.get(i)); //store each element as a double in the array
                }
            }


        } catch (FileNotFoundException e) { //error handling, in case file does not exist in the folder.
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void Calculations() {

        //Calculations() - USED FOR QUESTION 1 ("Last Year Stats and Report" Tab) to calculate and assign data to class variables,
        //the variables are then used when generating a .txt or .csv report, also this method sets the Labels, which display
        //Station's highest tmax, lowest tmin, total airfrost days, total rainfall (Question 1 requirements).
        //Method loops through the converted double arrays, (populated with data from csvReader()) and comparing
        //indexed values determines highest tMax and lowest tMin, also by summing-up values, Total Rainfall and Air Frost Days.

        //CHECKS IF THE CSV FILE IS NOT EMPTY

        if (!inputMax.isEmpty() & !inputMin.isEmpty() & !inputAF.isEmpty() & !inputRainfall.isEmpty()) {

            // HIGHEST tMAX:
            this.theMax = this.convertedMax[0];
            for (int i = 0; i < this.convertedMax.length; i++) {
                if (this.convertedMax[i] > this.theMax) {
                    this.theMax = this.convertedMax[i];
                }
            }
            tMax.setText(String.valueOf(this.theMax)); //sets the figure label for "HIGHEST MONTHLY MEAN MAXIMUM TEMPERATURE (tMAX)"

            // LOWEST tMIN:
            this.theMin = this.convertedMin[0];
            for (int i = 0; i < this.convertedMin.length; i++) {
                if (this.convertedMin[i] < this.theMin) {
                    this.theMin = this.convertedMin[i];
                }
            }
            tMin.setText(String.valueOf(this.theMin)); ///sets the figure label for "LOWEST MONTHLY MEAN MINIMUM TEMPERATURE  (tMIN)"

            // TOTAL AF
            double totalAF = 0;
            for (int i = 0; i < this.convertedTotalAF.length; i++) {
                totalAF += this.convertedTotalAF[i];
            }
            //casted number:
            this.castedAF = (int) totalAF; //to get an integer value of days
            AF.setText(String.valueOf(this.castedAF));  //sets the figure label for "TOTAL AIR FROST DAYS"


            // TOTAL RAINFALL
             this.totalRainfall = 0.0;
            for (int i = 0; i < this.convertedTotalRainfall.length; i++) {
                this.totalRainfall += this.convertedTotalRainfall[i];
            }

            //FIXING THE ROUNDING OF VALUE
            Long totalRainfallrounded = Math.round(this.totalRainfall * 10);
            this.totalRainfall = totalRainfallrounded / 10.0;

            Rainfall.setText(String.valueOf(this.totalRainfall)); //sets the figure label for "TOTAL RAINFALL"
        } else {
            //IF THE DATA FOR 2019 YEAR IS MISSING  THE FIGURE LABEL WILL BE SET WITH FOLLOWING MESSAGES:
            Rainfall.setText("CSV is Missing Data");
            AF.setText("CSV is Missing Data");
            tMin.setText("CSV is Missing Data");
            tMax.setText("CSV is Missing Data");
        }

    }



    private void buttonChecker() {
        //buttonChecker - used in Question 2 ("Year Comparison"), when checking if a year 1 in ("Selected Year:") Dropdwon menu has already been selected, thus disabling
        // it in year 2 ("Compare to Year(s):") and preventing data duplication.


        //ALGORITHM LOOPS THROUGH THE MENU ITEMS IN SECOND YEAR ("Compare to Year(s):"), iF THAT IS EQUALS TO YEAR in ("Selected Year:"), WE DISABLE IT.
        for (MenuItem searchItem : chartYearCompare.getItems()) {
            searchItem.setDisable(false);
            if (searchItem.getText().equals(year)) { //year - class variable gets set when year 1 is selected in ("Selected Year:")
                searchItem.setDisable(true);
            }
        }
    }

    private void barchartNumberAxis() {
        //for QUESTION 2

        //barchartNumberAxis() - is an implemented fix, which works around JavaFX's BarChart limitation - X axis has to be a CategoryAxis,
        //However for this App's design it needs to be a NumberAxis, representing months in integers (12).
        //Therefore on initialization or after clearing the charts barchartNumber - creates an empty BarChart and working axis.

        //PLACE HOLDER FOR THE BAR CHART, ALLOWS ANIMATION AND PRESERVES CATEGORY AXIS, as integers
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();


        for (int i = 0; i < 12; i++) {
            series2.getData().add(new XYChart.Data((i + 1) + "", 0));
            series3.getData().add(new XYChart.Data((i + 1) + "", 0));
        }

        tMaxChart.getData().add(series2);
        tMinChart.getData().add(series3);
        tMaxChart.setLegendVisible(false);
        tMinChart.setLegendVisible(false);

    }


    private void plotCharts(String labelYear) {

        //USED FOR QUESTION 2 -
        // plotCharts() - takes year data either from "Meteorological Station", "Selected year" or "Compare to Year(s)" Event Handlers.
        //based on the passed in year, plotCharts Creates Total Rainfall, tMax, tMin, Airfrost Days, Charts

        //  Rainfall LINECHART
        XYChart.Series series1 = new XYChart.Series();
        // tMAX BARCHART
        XYChart.Series series2 = new XYChart.Series();
        // tMIM BARCHART
        XYChart.Series series3 = new XYChart.Series();
        // AF LINECHART
        XYChart.Series series4 = new XYChart.Series();


        //Loops through the converted total rainfall, populated once the year has been passed into csvReader(),
        //x axis is populated accordingly to amount of items in the iterable object, while y axis populates the objects values.
        for (int i = 0; i < convertedTotalRainfall.length; i++) {
            series1.getData().add(new XYChart.Data(i + 1, convertedTotalRainfall[i]));
        }

        RainfallChart.setLegendVisible(true); //sets the legend to be visible.
        RainfallChart.getData().add(series1); //series containing the data is plotted on the chart.
        series1.setName(labelYear); // SETS THE SERIES NAME IN THE LEGEND



        for (int i = 0; i < monthsInaYear; i++) {
            if(i < convertedMax.length){        //if month is lesser than the amount of tMax values for that year, then populate the value as normal
                series2 .getData().add(new XYChart.Data((i + 1) + "", convertedMax[i]));
            }
            else{ //otherwise when there are less than 12 months of data available, populate remaining data as 0
                series2 .getData().add(new XYChart.Data((i + 1) + "", 0));
            }
        }
//
        tMaxChart.setLegendVisible(true);
        tMaxChart.getData().add(series2); //Plots the BarChart
        series2.setName(labelYear); // SETS THE SERIES NAME IN THE LEGEND


        //Same repeated approach for tMin:
        for (int i = 0; i < monthsInaYear; i++) {
            if(i < convertedMin.length){
                series3.getData().add(new XYChart.Data((i + 1) + "", convertedMin[i]));
            }
            else{
                series3.getData().add(new XYChart.Data((i + 1) + "", 0));
            }

        }
        tMinChart.setLegendVisible(true);
        tMinChart.getData().add(series3);
        series3.setName(labelYear); // SETS THE SERIES NAME IN THE LEGEND



        for (int i = 0; i < convertedTotalAF.length; i++) {
            series4.getData().add(new XYChart.Data((i + 1), convertedTotalAF[i]));
        }

        AFChart.setLegendVisible(true);
        AFChart.getData().add(series4);
        series4.setName(labelYear); // SETS THE SERIES NAME IN THE LEGEND


    }


    private void clearCharts() {

        //clearCharts() - clears chart data in Question 2.

        RainfallChart.getData().clear();
        tMaxChart.getData().clear();
        tMinChart.getData().clear();
        AFChart.getData().clear();




    }

    private void emptyDataAlert(String year){

        //USED IN QUESTION 2
        // Method adopted based on: https://stackoverflow.com/questions/31899275/difference-between-optionalbuttontype-get-and-alert-getresult
        // Authors: Brad Turek (2012), Uluk Biy (2015)

        //If the selected Year for the station either in "Selected Year:" or "Compare to Year(s):" yields no data from the .csv file,
        //then the following alert is displayed:

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "CSV Data for year " + year + " is missing for this station");

        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> alert.hide());
    }


    private void reportCalculations() {

        //FOR QUESTION 3 - reportCalculations - calculates for each station's tMax's & tMin's year/month, also
        //Calculates weighted averages for Air Frost Days and Rainfall. Method assigns this data to dedicated class variables,
        //from which the data is used when writing the report into .csv or .txt.


        // HIGHEST tMAX:

        // similar to Calculations(); find the tMax value, and also the max values index
        double theMax = this.convertedMax[0];
        int tMaxIndex = 0;
        for (int x = 0; x < this.convertedMax.length; x++) {
            if (this.convertedMax[x] > theMax) {
                theMax = this.convertedMax[x];
                tMaxIndex = x;
            }
        }

        tMaxYear = years.get(tMaxIndex); //using the set up tMaxIndex, can use it the years List, and corresponding value will return the tMax values year.
        tMaxMonth = months.get(tMaxIndex); //same process as for years.


        // LOWEST tMIN:

        //Same process as for tMAX, however finding the lowest value and its index.
        double theMin = this.convertedMin[0];
        int tMinIndex = 0;
        for (int y = 0; y < this.convertedMin.length; y++) {
            if (this.convertedMin[y] < theMin) {
                theMin = this.convertedMin[y];
                tMinIndex = y;
            }
        }

        tMinYear = years.get(tMinIndex);
        tMinMonth = months.get(tMinIndex);


        // MEAN ANNUAL AF

        //finding the total AF, by adding up all the values in convertedTotalAF array.
        double totalAF = 0;
        for (int z = 0; z < this.convertedTotalAF.length; z++) {
            totalAF += this.convertedTotalAF[z];
        }

        double numberofYears = years.size() / 12; //calculate the amount of years to filter out repetition of months.


        //Calculating Average Annual Air Frost Days,
        averageAnnualAF = Math.round(totalAF * 10 / numberofYears) / 10.0; //DECIMAL FORMATTING


        // MEAN ANNUAL RAINFALL

        //Finding the total Rainfall, by adding up all the values in convertedTotalRainfall array.
        double totalRainfall = 0;
        for (int j = 0; j < this.convertedTotalRainfall.length; j++) {
            totalRainfall += this.convertedTotalRainfall[j];
        }

        averageAnnualRainfall = Math.round(totalRainfall * 10 / numberofYears) / 10.0; //DECIMAL FORMATTING
    }

    public void labelHover(){

        //FOR QUESTIONS 2 method used as extra functionality, to enhance data point's visibility and precision on different types of charts.
        //This is particularly useful when comparing close station's different year data.

        //MOUSE EVENT Method Adopted from https://stackoverflow.com/questions/25892695/tooltip-on-line-chart-showing-date/25906039
        // Author: ItachiUchiha (2014)

        // Set new variable, which equals to the FXML injected Line Chart object.
        final LineChart<String, String> bc = RainfallChart;

        //using Tooltip, for each item's value in RainfallChart, assigns its x and y co-ordinates on a MOUSE HOVER effect.
        for (XYChart.Series<String, String> s : bc.getData()) {
            for (XYChart.Data d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        "Month : "+ d.getXValue().toString() + "\n" + //the displayed hover table shows "Month" equal to item's x value
                                "Rainfall : " + d.getYValue())); //and the "Rainfall" as item's y value.

            }
        }

        //SAME PROCESS IS REPEATED FOR THE REMAINING QUESTION 2 CHARTS.
        final LineChart<String, String> b2 = AFChart;

        for (XYChart.Series<String, String> s : b2.getData()) {
            for (XYChart.Data d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        "Month : "+ d.getXValue().toString() + "\n" +
                                "AF Days: " + d.getYValue()));

            }
        }
        final BarChart<String, String> b3 = tMaxChart;

        for (XYChart.Series<String, String> s : b3.getData()) {
            for (XYChart.Data d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        "Month : "+ d.getXValue().toString() + "\n" +
                                "tMax: " + d.getYValue()));

            }
        }
        final BarChart<String, String> b4 = tMinChart;

        for (XYChart.Series<String, String> s : b4.getData()) {
            for (XYChart.Data d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        "Month : "+ d.getXValue().toString() + "\n" +
                                "tMin: " + d.getYValue()));

            }
        }

    };

//########################################### ALL REPETITIVE APPLICATION'S FUNCTIONS (Methods) [ENDS HERE] #########################################

    //WHEN APPLICATION IS STARTED THE FOLLOWING METHODS RUNS:

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //THE FOLLOWING METHOD TO DYNAMICALLY LOAD CSV STATION FILES HAS BEEN ADOPTED FROM
        // https://mkyong.com/java/java-files-walk-examples/
        //Author: mkyong (2018)

        try (
                //Retrieves the files from "csv" folder
                Stream<Path> walk = Files.walk(Paths.get(getClass().getResource("/csv/").toURI()))) {

            // result List, is populated with all the file path directories in a form of a string, inside the "csv" folder of files that end with ".csv",

            List<String> result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".csv")).collect(Collectors.toList());


            for (int i = 0; i < result.size(); i++) {

                // the following method Loops through 'results' containing each directory string ending with the station's name
                //then each station's name gets extracted from directory string at the characters starting "/" and ending "."

                String fullPath = result.get(i);
                int index = fullPath.lastIndexOf("/");
                String fileName = fullPath.substring(index + 1);

                String station = fileName.substring(0, fileName.lastIndexOf('.'));
                csvNames.add(station); //lastly the extracted Station Names are added into a Class variable List - csvNames.

            }

            //SORT ALL VALUES IN THE csvNames alphabetically, in order to display them ordered in all station name DROPDOWN MENUS
            Collections.sort(csvNames);

            //Loop through csvNames and create 3 MenuItem objects, because there are 3 tabs and 3 DROPDOWN MENUS for Selecting Stations name.
            for (int i = 0; i < csvNames.size(); i++) {

                MenuItem menuItem = new MenuItem(csvNames.get(i)); //used for Question 1
                MenuItem menuItem2 = new MenuItem(csvNames.get(i));//used for Question 2
                MenuItem menuItem3 = new MenuItem(csvNames.get(i));//used for Question 3

//#################################### FOR QUESTION 1 - "LAST YEAR STATS AND REPORTS" (2019) ####################################################

            //Each menuItem (Station) is essentially a button in the dropdown menu, that has the following Action when clicked:

                menuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        //GETTING MenuItem's text, adopted approach from: //https://stackoverflow.com/questions/9029162/how-do-i-get-the-text-of-a-button-in-java
                        //Author: Sean (2012)

                        Object o = event.getSource(); //retrieving the source of clicked item
                        MenuItem b = null;

                        if (o instanceof MenuItem)
                            b = (MenuItem) o;

                        if (b != null)
                            StationName = b.getText(); //Setting the class String variable StationName to the clicked stations name.

                        //Setting the Label of selected station
                        selectedStation.setText("Selected Station: " + StationName);

                        //Passing in the Station's name and year 2019 as latest year to compute the data from correct .csv file corresponding
                        //to the clicked Station's name.
                        csvReader("2019", StationName);

                        //Data has been populated to class variables, from the station name matching  .csv file,
                        //Following method calculates the tMax, tMin, Total AF days, Total Rainfall.
                        Calculations();

                    }

                });

                //All station names (MenuItem buttons) get added into the Question 1's Meteorological Stations Dropdown menu.
                question1menubar.getItems().add(menuItem);

//###################################################################### FOR QUESTION 2 - "YEAR COMPARISON" #####################################################

               // Same for loop continues, setting the action handler same as for menuItem2

                menuItem2.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        //GETTING MenuItem's text, adopted approach the same approach as for menuItem in Question 1

                        Object o = event.getSource();
                        MenuItem b = null;
                        String buttonText = "";

                        if (o instanceof MenuItem)
                            b = (MenuItem) o;

                        if (b != null)
                            buttonText = b.getText();


                        StationName2 = buttonText; //Setting the selected station's name in Question 2.
                        Q2SelectedStation.setText("Selected Station: " + StationName2);

                        //setting the selected year to the value held in class variable year
                        selectedYear1.setText("Selected Year: " + year);

                        //comparable years are refreshed/deselected, once the station is changed
                        selectedYear2.setText("Compare to Year(s): ");
                        q2CompareYears.clear();

                        //checking whether year 2 "Compare to Year(s):" has been selected, to avoid duplication
                        buttonChecker();


                        //Similar to menuItem:
                        //Passing in the Station's name and selected year to compute the data from correct .csv file corresponding
                        //to the clicked Station's name.
                        csvReader(year, StationName2);

                        //THE FOLLOWING WILL BE COMPUTED ONLY IF THE SELECTED STATION's CSV file is not empty
                        if (!inputMax.isEmpty() & !inputMin.isEmpty() & !inputAF.isEmpty() & !inputRainfall.isEmpty()) {

                            clearCharts(); //Restarts the Question 2 charts, every time station is changed
                            plotCharts(year); //The charts are re-populated with Data from the selected station (due to csvReader) and year 1 is passed
                            labelHover(); //label Hover function is activated for additional precision to the user.


                        } else { //IF THE STATION'S CSV FILE IS EMPTY:

                            clearCharts(); //CHARTS ARE CLEARED, no new data is plotted
                            barchartNumberAxis(); //FIX for the empty BarChart is applied, otherwise X axis crashes.
                            emptyDataAlert(year); //Alert Box is triggered, display's message to user that there is no Data for the particular year selected for the station.

                        }

                    }
                });


                question2menubar.getItems().add(menuItem2); //same as menuItem for Q1, menuItem2 (Station name) is added in the Menu Dropdown menu.

//###################################################################### FOR QUESTION 3 - "STATION COMPARISON AND REPORT" #####################################################

                // Same for loop continues, setting the action handler same as for menuItem3

                menuItem3.setOnAction(new EventHandler<ActionEvent>() {

                    //GETTING MenuItem's text, adopted approach the same approach as for menuItem in Question 1 & 2

                    @Override
                    public void handle(ActionEvent event) {
                        Object o = event.getSource();
                        MenuItem b = null;
                        String buttonText = "";

                        if (o instanceof MenuItem)
                            b = (MenuItem) o;

                        if (b != null)
                            buttonText = b.getText();



                        //WHEN CLICKED DISABLED AVOID DUPLICATES
                        menuItem3.setDisable(true);


                        StationName3 = buttonText; //Station name for Question 3 gets assigned the selected stations text.

                        //get all data for selected station
                        csvReader("all", StationName3);

                        //THE FOLLOWING WILL BE COMPUTED ONLY IF THE SELECTED STATION's CSV file is not empty
                        if (!inputMax.isEmpty() & !inputMin.isEmpty() & !inputAF.isEmpty() & !inputRainfall.isEmpty()) {

                            reportCalculations(); //run the calculations for Question 3 requirements, that are used for generating reports. This method
                            //updates the designated class variables, used below.

                            //class variable, holding Average Annual Rainfall's series is populated with the Selected stations name and corresponding
                            //average annual rainfall, in order to maintain a fixed value for more convenient comparison between stations.

                            seriesAnnualRainfall.getData().add(new XYChart.Data(StationName3+" ("+averageAnnualRainfall+")", averageAnnualRainfall));
                            seriesAnnualAF.getData().add(new XYChart.Data(StationName3+" ("+averageAnnualAF+")", averageAnnualAF));


                            //To avoid duplication error, chart data gets cleared
                            Q3RainfallChart.getData().clear();
                            Q3AFChart.getData().clear();

                            // AND repopulated with the newly updated series, which contain additional data if more stations are selected.
                            Q3RainfallChart.getData().add(seriesAnnualRainfall);
                            Q3AFChart.getData().add(seriesAnnualAF);


                            //MOUSE EVENT Method Adopted from https://stackoverflow.com/questions/25892695/tooltip-on-line-chart-showing-date/25906039
                            // Author: ItachiUchiha (2014)

                            final BarChart<String, String> bc = Q3RainfallChart;

                            for (XYChart.Series<String, String> s : bc.getData()) {
                                for (XYChart.Data d : s.getData()) {
                                    Tooltip.install(d.getNode(), new Tooltip(
                                            d.getXValue().toString() + "\n" +
                                                    "Average Annual Rainfall : " + d.getYValue()));

                                }
                            }
                            //MOUSE EVENT https://stackoverflow.com/questions/25892695/tooltip-on-line-chart-showing-date/25906039
                            final BarChart<String, String> bc2 = Q3AFChart;

                            for (XYChart.Series<String, String> s : bc2.getData()) {
                                for (XYChart.Data d : s.getData()) {
                                    Tooltip.install(d.getNode(), new Tooltip(
                                            d.getXValue().toString() + "\n" +
                                                    "Average Annual Air Frost Days : " + d.getYValue()));

                                }
                            }


                            seriesAnnualRainfall.setName(StationName3); // SETS THE SERIES NAME IN THE LEGEND
                            Q3RainfallChart.setLegendVisible(false);

                            seriesAnnualAF.setName(StationName3); // SETS THE SERIES NAME IN THE LEGEND
                            Q3AFChart.setLegendVisible(false);

                        }


                        else{

                            //Alert Box is triggered, display's message to user that there is no Data for the selected station.
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Station "+StationName3+" has no data.");

                            alert.showAndWait()
                                    .filter(response -> response == ButtonType.OK)
                                    .ifPresent(response -> alert.hide());
                        }

                    }

                });

                question3menubar.getItems().add(menuItem3); //same as menuItem,menuItem2 (Station name) is added in the Menu Dropdown menu. for Q3

            } //FOR LOOP ENDS DROPDOWN MENUS for Q1,Q2,Q3 Completed, each station is a button with event handler.


//#######################################  FOR QUESTION 2  - CHART 1st YEAR MENU-BUTTON  ("Selected Year:")   ####################################################################



            //For years 2011-2019 create Menu buttons in Question 2
            for (int i = 1; i < 10; i++) {
                MenuItem yearItem = new MenuItem("201" + i);

                chartYear.getItems().add(yearItem);

                yearItem.setOnAction(new EventHandler<ActionEvent>() { // each year in the Dropdown menu under label "Selected Year: " is a clickable button, with following action:
                    @Override
                    public void handle(ActionEvent event) {

                        //GETTING MenuItem's text, adopted approach the same approach as for menuItems in Q1,Q2,Q3

                        Object o = event.getSource();
                        MenuItem b = null;
                        String newYear ="";


                        if (o instanceof MenuItem)
                            b = (MenuItem) o;

                        if (b != null)
                            newYear = b.getText(); //selected year is assigned to an empty local String variable.


                        csvReader(newYear, StationName2); //csvReader receives the selected station name, with selected year from the  ("Selected Year:") Dropdown menu.




                        //THE FOLLOWING WILL BE COMPUTED ONLY IF THE SELECTED STATION's CSV file is not empty
                        if (!inputMax.isEmpty() & !inputMin.isEmpty() & !inputAF.isEmpty() & !inputRainfall.isEmpty()) {

                            year = newYear; //year class variable is set to the 1st selected year from ("Selected Year:") Dropdown menu.

                            //CLEAR and UPDATE LABELS
                            selectedYear1.setText("Selected Year: " + year);
                            selectedYear2.setText("Compare to Year(s): ");
                            q2CompareYears.clear();
                            Q2SelectedStation.setText("Selected Station: " + StationName2);

                            //ENSURE ONLY 1 YEAR in ("Selected Year:") is activated, therefore loop through ("Selected Year:") and set disable to false.
                            for (MenuItem searchItem : chartYear.getItems()) {
                                searchItem.setDisable(false);
                            }

                            //ONLY SET DISABLE TRUE on the selected item.
                            yearItem.setDisable(true);
                            buttonChecker(); // DISABLE THE SAME SELECTED BUTTON IN 2nd year ("Compare to Year(s)") Dropdown menu.

                            clearCharts(); //Reset all the charts once a year has been selected
                            plotCharts(year); //pass in the Selected year and based on that year plot the charts
                            labelHover(); //label Hover function is activated for additional precision to the user. on all years selected in year 1.


                        } else {

                            //Alert Box is triggered, display's message to user that there is no Data for the particular year selected for the station.
                            emptyDataAlert(newYear);


                        }

                    }
                });


                ///####################################################  CHART 2nd YEAR  MENU-BUTTON ###########################################################

                //FOR LOOP CONTINUES, ANOTHER SET OF YEARS IS CREATED (2nd Year under "Compare to Year(s):" label)

                MenuItem yearItem2 = new MenuItem("201" + i);

                chartYearCompare.getItems().add(yearItem2);

                yearItem2.setOnAction(new EventHandler<ActionEvent>() { //EACH YEAR is a button with event handler
                    @Override
                    public void handle(ActionEvent event) {

                        csvReader(year, StationName2); //RUN AGAIN csvReader with 1st Year and Station Name passed in
                        //this prevents from the comparison year to be selected prior to Station or year 1 being selected, since the purpose
                        //of this year is to be an object to compare against the 1st year.

                        //AGAIN If the Station's csv is not empty:
                        if (!inputMax.isEmpty() & !inputMin.isEmpty() & !inputAF.isEmpty() & !inputRainfall.isEmpty()) {

                            //GETTING MenuItem's text, adopted approach the same approach as for menuItems in Q1,Q2,Q3


                            Object o = event.getSource();
                            MenuItem b = null;


                            if (o instanceof MenuItem)
                                b = (MenuItem) o;

                            if (b != null)
                                year2 = b.getText(); //CLASS variable year2 is set for the first time.


                            csvReader(year2, StationName2); //changing the class variables based on selected year 2 and the same Station name as previously selected.


                            //IF at least one of the lists is empty then dont compute the below.
                            if (!inputMax.isEmpty() & !inputMin.isEmpty() & !inputAF.isEmpty() & !inputRainfall.isEmpty()) {

                                plotCharts(year2); //plot charts based on year 2 data
                                q2CompareYears.add(year2); //for the labels, since it is possible to compare to more than just 1 year, the label list is populated with years selected in Year 2 Dropdown menu

                                //SET LABELS
                                String result = String.join(", ", q2CompareYears);
                                selectedYear2.setText("Compare to Year(s): " + result);
                                Q2SelectedStation.setText("Selected Station: " + StationName2);

                                yearItem2.setDisable(true); //Disable the selected year to avoid duplication


                                labelHover(); //label Hover function is activated for additional precision to the user on all selected from year 2.


                            } else {

                                emptyDataAlert(year2); //If the selected year is empty in the csv file, the alert box is triggered.

                            }

                        }else{ //If the year 2 is selected before Station or Year 1 is selected then Alert box is triggered:
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please first select a year to compare to.");

                            alert.showAndWait()
                                    .filter(response -> response == ButtonType.OK)
                                    .ifPresent(response -> alert.hide());
                        }


                    }

                });
            }

            //ENSURE FIRST ITEM IN MENU IS SELECTED TO THE FIRST YEAR.
            chartYear.getItems().get(0).setDisable(true);

            //CLEAR DATA BUTTON FOR QUESTION 2
            Q2clearData.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    //WHEN THE BUTTON IS CLICKED:
                    clearCharts(); //Q2 Charts get cleared.

                    //LABELS are also cleared.
                    Q2SelectedStation.setText("Selected Station:");
                    selectedYear1.setText("Selected Year:");
                    selectedYear2.setText("Compare to Year(s):");
                    q2CompareYears.clear();

                    //STATION NAME is reset to empty string.
                    StationName2="";

                    //ALL DROPDOWN YEAR1 and YEAR2 MenuItem buttons are reset back to default stage, setDisable(false).
                    for (MenuItem searchItem : chartYear.getItems()) {
                        searchItem.setDisable(false);
                    }

                    for (MenuItem searchItem : chartYearCompare.getItems()) {
                        searchItem.setDisable(false);
                    }

                    barchartNumberAxis(); //Empty Barchart X axis Fix is activated.

                    chartYear.getItems().get(0).setDisable(true); //ENSURE FIRST ITEM IN YEAR1 DROPDOWN MENU IS SELECTED TO THE FIRST YEAR.
                    year = chartYear.getItems().get(0).getText(); // ENSURE year class variable is set to this selection.


                }
            });

            //CLEAR DATA BUTTON FOR QUESTION 3
            Q3clearData.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    //ALL STATION NAMES in THE DROPDOWN MENU reset to default - deselected.
                    for (MenuItem searchItem : question3menubar.getItems()) {
                        searchItem.setDisable(false);
                    }

                    //CLEAR Q3 CHARTS
                    Q3RainfallChart.getData().clear();
                    Q3AFChart.getData().clear();

                    //CLEAR Q3 CHART SERIES
                    seriesAnnualRainfall.getData().clear();
                    seriesAnnualAF.getData().clear();

                }
            });


            //FIXING THE BARCHART CATEGORY AXIS ON INITIALIZED APP.
            barchartNumberAxis();




        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }




//  ######################################################################## QUESTION 3 - "STATION COMPARISON AND REPORT" ######################################################

        //"Generate Text Report" Button when clicked:
        generateReport.setOnAction(e -> {

            try {
                //Create a Filewriter object and a write to a file named "Report_2.txt"
                FileWriter writer = new FileWriter(("Report_2.txt"));


                String stationtxt=""; //placeholder for Station name.

                //Loop through all stations and pass them in the CSV reader, with all year data.
                for (int i = 0; i < csvNames.size(); i++) {
                    stationtxt = csvNames.get(i);
                    csvReader("all", stationtxt);

                    //IF THE CSV has data:
                    if (!inputMax.isEmpty() & !inputMin.isEmpty() & !inputAF.isEmpty() & !inputRainfall.isEmpty()) {

                        reportCalculations(); //run calculations based on the Station's csv file.

                        //Write the following lines into the "Report_2.txt" file, for every station.
                        writer.write("Number: " + i + "\n"); //number inside the report.
                        writer.write("Station: " + stationtxt + "\n"); //stations name
                        writer.write("Highest: " + tMaxMonth + "/" + tMaxYear + "\n"); //tmax month and year
                        writer.write("Lowest: " + tMinMonth + "/" + tMinYear + "\n"); //tmin month and year
                        writer.write("Average annual af: " + averageAnnualAF + "\n"); //average annual AF days
                        writer.write("Average annual rainfall: " + averageAnnualRainfall + "\n"); //average annual rainfall
                        writer.write("\n");

                    }
                }

                writer.close(); //close the writer.

                //Alert the user the report has been created.

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Text Report 'Report_2.txt' has been Created");

                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(response -> alert.hide());

            } catch (IOException ex) {
                ex.printStackTrace();
            }


        });
//################################################################# Q3 ADDITIONAL FEATURE - CSV REPORT FORMATTING ##################################

        //"Generate CSV Report" Button when clicked:
        generateReportCSV.setOnAction(e ->{

            try {
                FileWriter writer = new FileWriter(("Report_2.csv")); //Same as Text Report, just different file "Report_2.csv"

                //CREATE HEADINGS IN THE CSV FILE.
                writer.write("SEQUENCE_NUMBER,STATION_NAME,HIGHEST,LOWEST,AVERAGE_ANNUAL_AF,AVERAGE_ANNUAL_RAINFALL"+"\n");

                //SAME PROCESS AS FOR TEXT REPORT
                String stationcsv ="";
                for (int i = 0; i < csvNames.size(); i++) {
                    stationcsv = csvNames.get(i);
                    csvReader("all", stationcsv);

                    if (!inputMax.isEmpty() & !inputMin.isEmpty() & !inputAF.isEmpty() & !inputRainfall.isEmpty()) {

                        reportCalculations();

                        //EACH LINE IS WRITTEN IN THE FILE TO MATCH The first Line (HEADINGS LINE)

                        writer.write(i+","+stationcsv.toUpperCase()+","+ tMaxMonth + "/" + tMaxYear+","+tMinMonth + "/" + tMinYear+","+averageAnnualAF+","+averageAnnualRainfall+"\n");

                    }
                }

                writer.close();

                //ONCE THE REPORT IS CREATED the ALERT IS TRIGERRED SAME WAY as for TEXT report
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "CSV Report 'Report_2.csv has been Created");

                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(response -> alert.hide());

            } catch (IOException ex) {
                ex.printStackTrace();
            }


        });

//################################################################# Q1 ADDITIONAL FEATURE - CSV REPORT FORMATTING ##################################

        //"Generate CSV Report" Button when clicked in QUESTION 1: (SAME PROCESS AS FOR Q3 CSV Report, differences commented below:
        Q1CSVReport.setOnAction(e ->{

            try {
                FileWriter writer = new FileWriter(("Report_1.csv"));

                //FIRST LINE IS USED as per the Calculations() method instead of as per Question 3 reportCalculations():

                writer.write("SEQUENCE_NUMBER,STATION_NAME,tMAX,tMIN,TOTAL_AF,TOTAL_RAINFALL"+"\n");


                for (int i = 0; i < csvNames.size(); i++) {
                    StationName = csvNames.get(i);
                    csvReader("2019", StationName);

                    if (!inputMax.isEmpty() & !inputMin.isEmpty() & !inputAF.isEmpty() & !inputRainfall.isEmpty()) {

                        Calculations(); //Calculations instead of reportCalculations (different content based on different question 1 requirements)

                        writer.write(i+","+StationName.toUpperCase()+","+ theMax +","+ theMin +","+castedAF +"," + totalRainfall +"\n");

                        //Set the Labels to 0 in Q1 for selected station once the Report has been generated.
                        tMax.setText("0");
                        tMin.setText("0");
                        Rainfall.setText("0");
                        AF.setText("0");

                    }
                }

                writer.close();

                //Alert Report_1.csv has been created.

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "CSV Report 'Report_1.csv' has been Created");

                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(response -> alert.hide());

            } catch (IOException ex) {
                ex.printStackTrace();
            }


        });


//################################################################# Q1 ADDITIONAL FEATURE - TEXT REPORT FORMATTING ##################################
        Q1TXTReport.setOnAction(e ->{ //SAME AS TEXT REPORT FOR Q3, differences commented below:

            try {
                FileWriter writer = new FileWriter(("Report_1.txt"));



                for (int i = 0; i < csvNames.size(); i++) {
                    StationName = csvNames.get(i);
                    csvReader("2019", StationName); //instead of all years, select only 2019

                    if (!inputMax.isEmpty() & !inputMin.isEmpty() & !inputAF.isEmpty() & !inputRainfall.isEmpty()) {

                        Calculations(); //Calculations instead of reportCalculations (different content based on different question 1 requirements)

                        writer.write("Number: " + i + "\n");
                        writer.write("Station: " + StationName.toUpperCase() + "\n");
                        writer.write("Highest Monthly Mean (tMax): " + theMax + "\n");
                        writer.write("Lowest Monthly Mean (tMin):: " + theMin + "\n");
                        writer.write("Total Air Frost Days: " + castedAF + "\n");
                        writer.write("Total Rainfall: " + totalRainfall + "\n");
                        writer.write("\n");

                        //Set the Labels to 0 in Q1 for selected station once the Report has been generated.
                        tMax.setText("0");
                        tMin.setText("0");
                        Rainfall.setText("0");
                        AF.setText("0");

                    }
                }

                writer.close();

                //Alert Report_1.csv has been created.
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Text Report 'Report_1.txt' has been Created");

                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(response -> alert.hide());

            } catch (IOException ex) {
                ex.printStackTrace();
            }


        });


    }

}











