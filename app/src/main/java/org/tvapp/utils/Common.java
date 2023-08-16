package org.tvapp.utils;

import android.content.Context;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;

import com.google.gson.Gson;

import org.tvapp.model.DataModel;
import org.tvapp.presenter.ImageCardPresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Common {

    public static DataModel getData(Context context) {
        Gson gson = new Gson();
        InputStream input = null;
        try {
            input = context.getAssets().open("movies.json");
            BufferedReader br =new BufferedReader(new InputStreamReader(input));
            return gson.fromJson(br, DataModel.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ListRow createListRow( String headerName, List<DataModel.Detail> items) {
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new ImageCardPresenter(true));
        for (int i = 0; i < items.size(); i++) {
            listRowAdapter.add(items.get(i));
        }
        HeaderItem header = new HeaderItem(0, headerName);
        return new ListRow(header, listRowAdapter);
    }

    public static List<String> getGenreList(){
        List<String> genreList = new ArrayList<>();
        genreList.add("All");
        genreList.add("Action");
        genreList.add("Adventure");
        genreList.add("Animation");
        genreList.add("Comedy");
        genreList.add("Crime");
        genreList.add("Documentary");
        genreList.add("Drama");
        genreList.add("Family");
        genreList.add("Fantasy");
        genreList.add("History");
        genreList.add("Horror");
        genreList.add("Music");
        genreList.add("Mystery");
        genreList.add("Romance");
        genreList.add("Science Fiction");
        genreList.add("TV Movie");
        genreList.add("Thriller");
        genreList.add("War");
        genreList.add("Western");
        return genreList;
    }

    public static List<String> getRegionList(){
        List<String> regionList = new ArrayList<>();
        regionList.add("All");
        regionList.add("Argentina");
        regionList.add("Australia");
        regionList.add("Austria");
        regionList.add("Belgium");
        regionList.add("Brazil");
        regionList.add("Bulgaria");
        regionList.add("Canada");
        regionList.add("China");
        regionList.add("Colombia");
        regionList.add("Czech Republic");
        regionList.add("Denmark");
        regionList.add("Finland");
        regionList.add("France");
        regionList.add("Germany");
        regionList.add("Greece");
        regionList.add("Hong Kong");
        regionList.add("Hungary");
        regionList.add("India");
        regionList.add("Indonesia");
        return regionList;
    }

    public static List<String> getYearList() {
        List<String> yearList = new ArrayList<>();
        yearList.add("All");
        yearList.add("2023");
        yearList.add("2022");
        yearList.add("2021");
        yearList.add("2020");
        yearList.add("2019");
        yearList.add("2018");
        yearList.add("2017");
        yearList.add("2016");
        return yearList;
    }

    public static List<String> getRatingList() {
        List<String> ratingList = new ArrayList<>();
        ratingList.add("All");
        ratingList.add(">=9.0");
        ratingList.add(">=8.0");
        ratingList.add(">=7.0");
        ratingList.add(">=6.0");
        ratingList.add(">=5.0");
        ratingList.add(">=4.0");
        ratingList.add(">=3.0");
        ratingList.add(">=2.0");
        ratingList.add(">=1.0");
        return ratingList;
    }

    public static List<String> getAudioList(){
        List<String> audioList = new ArrayList<>();
        audioList.add("All");
        audioList.add("EN");
        audioList.add("ES");
        audioList.add("FR");
        audioList.add("DE");
        audioList.add("IT");
        audioList.add("PT");
        audioList.add("RU");
        audioList.add("JA");
        audioList.add("ZH");
        audioList.add("HI");
        audioList.add("KO");
        audioList.add("AR");
        audioList.add("BN");
        audioList.add("CS");
        audioList.add("DA");
        audioList.add("EL");
        audioList.add("FA");
        audioList.add("FI");
        return  audioList;
    }
}
