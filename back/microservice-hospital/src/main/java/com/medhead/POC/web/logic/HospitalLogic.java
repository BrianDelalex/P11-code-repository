package com.medhead.POC.web.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

import com.medhead.POC.dao.HospitalRepository;
import com.medhead.POC.models.Hospital;
import com.medhead.POC.models.MatrixApiTypes.Response;
import com.medhead.POC.web.controller.HospitalController;

public class HospitalLogic {

    private Double Distance(Double lat1, Double lon1, Double lat2, Double lon2) {
        Double radlat1 = Math.PI * lat1 / 180;
        Double radlat2 = Math.PI * lat2 / 180;
        Double theta = lon1 - lon2;
        Double radtheta = Math.PI * theta / 180;
        Double dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
        dist = Math.acos(dist);
        dist = dist * 180 / Math.PI;
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return dist;
    }

    public Hospital GetNearestHospital(HospitalController controller, String speciality, String latitude, String longitude) {
        List<String> specialities = new ArrayList<String>();
        specialities.add(speciality);
        List<Hospital> hospitals = controller.repository.findBySpecialitiesIsContaining(specialities);

        hospitals = hospitals
            .stream()
            .filter(h -> h.getFreeBeds() > 0)
            .collect(Collectors.toList());
        if (hospitals.isEmpty())
            return null;
        List<Double> distances = new ArrayList<Double>();
        // Calculating distance between request position and hospitals.
        for (Hospital h : hospitals) {
            
            if (h.getLatitude() != null && h.getLongitude() != null) {
                distances.add(this.Distance(
                        Double.parseDouble(latitude),
                        Double.parseDouble(longitude),
                        Double.parseDouble(h.getLatitude()),
                        Double.parseDouble(h.getLongitude()))
                    );
            }
        }

        // Looking for the hospital with the shortest distance to the request
        Double shortest = 1000000.;
        int shortest_index = -1;
        int i = 0;
        for (Double d : distances) {
            if (shortest > d) {
                shortest = d;
                shortest_index = i;
            }
            i++;
        }

        // Filtering - Remove all hospitals with a distance difference greater than 50 compared to the shortest one
        List<Hospital> hospital_to_check = new ArrayList<Hospital>();
        hospital_to_check.add(hospitals.get(shortest_index));
        int j = 0;
        for (Double d : distances) {
            if (d - 50. < shortest && hospitals.get(j) != hospitals.get(shortest_index))
                hospital_to_check.add(hospitals.get(j));
            j++;
        }

        List<Response> responses = new ArrayList<Response>();
        for (Hospital h : hospital_to_check) {
            if (!hospital_to_check.isEmpty()) {
                HttpHeaders headers = new HttpHeaders();
                List<MediaType> mediaTypes = new ArrayList<MediaType>();;
                mediaTypes.add(MediaType.ALL);
                headers.setAccept(mediaTypes);
                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?destinations={destinations}&origins={origins}&key={key}";

                Map<String, String> params = new HashMap<>(Collections.singletonMap("destinations", h.getLatitude() + "," + h.getLongitude()));
                params.put("origins", latitude + "," + longitude);
                params.put("key", "AIzaSyCs6HGKrAjFRAzM_B3dVS50LrKcxboru54");

                HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, headers);
                HttpEntity<Response> response = controller.restTemplate.exchange(url, HttpMethod.GET, requestEntity, Response.class, params);

                responses.add(response.getBody());
            }
        }

        Response nearest = responses
            .stream()
            .min(Comparator.comparing(Response::GetDuration))
            .orElse(null);
        Hospital result = null;
        if (nearest != null) {
            i = 0;
            for (Response r : responses) {
                if (r == nearest) {
                    result = hospital_to_check.get(i);
                    break;
                }
                i++;
            }
        }

        return result;
    }
}
