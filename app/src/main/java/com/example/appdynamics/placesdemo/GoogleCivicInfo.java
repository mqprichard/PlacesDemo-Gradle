package com.example.appdynamics.placesdemo;

import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.civicinfo.CivicInfo;
import com.google.api.services.civicinfo.CivicInfoRequestInitializer;
import com.google.api.services.civicinfo.model.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2013 Mark Prichard
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class GoogleCivicInfo {
    private static final String TAG = GoogleCivicInfo.class.getName();

    static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    static final GsonFactory JSON_FACTORY = new GsonFactory();
    static final HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {
        @Override
        public void initialize(HttpRequest request) throws IOException {
        }
    };

    public void getInfo (String address) {
        try {
            GoogleClientRequestInitializer KEY_INITIALIZER =
                new CivicInfoRequestInitializer(GooglePlacesKey.API_KEY);

            CivicInfo civicInfo =
                new CivicInfo.Builder(HTTP_TRANSPORT, JSON_FACTORY, httpRequestInitializer)
                    .setApplicationName("Places")
                    .setGoogleClientRequestInitializer(KEY_INITIALIZER)
                    .build();

            RepresentativeInfoResponse representativeInfoResponse
                    = civicInfo.representatives()
                               .representativeInfoByAddress()
                               .setAddress(address)
                               .execute();

            Map<String, GeographicDivision> divisionMap = representativeInfoResponse.getDivisions();
            List<Office> offices = representativeInfoResponse.getOffices();
            List<Official> officials = representativeInfoResponse.getOfficials();

            Collection<GeographicDivision> divisions = divisionMap.values();
            Log.d(TAG, "Found " + divisions.size() + " divisions");
            Iterator<Official> officialIterator = officials.iterator();
            Log.d(TAG, "Found " + officials.size() + " officials");
            Iterator<Office> officeIterator = offices.iterator();
            Log.d(TAG, "Found " + offices.size() + " offices");
        }
        catch (IOException exception) {
        }
    }
}
