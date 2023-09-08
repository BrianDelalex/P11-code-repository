import { useEffect, useState } from 'react';
import GooglePlacesAutocomplete from 'react-google-places-autocomplete';
import Geocode from "react-geocode";

let googleApiKey = "AIzaSyCYTonJS0AVVKGGfYUys0rn76zIv9OObfc";
import './Home.css'

Geocode.setLocationType("ROOFTOP");
Geocode.setApiKey("AIzaSyAFGhXLO3WNapNNgFVYqk67_KDmh7NuTX4");

const Home = () => {
    const [location, setLocation] = useState(null);
    const [data, setData] = useState(null);

    function getLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(success, error);
        } else {
            console.log("Geolocation not supported");
        }
    }

    function success(position) {
        const latitude = position.coords.latitude;
        const longitude = position.coords.longitude;
        setLocation({ latitude, longitude });
        console.log(`Latitude: ${latitude}, Longitude: ${longitude}`);
        Geocode.fromLatLng(latitude,longitude).then(
            (response) => {
              const address = response.results[0].formatted_address;
              let city, state, country;
              for (let i = 0; i < response.results[0].address_components.length; i++) {
                for (let j = 0; j < response.results[0].address_components[i].types.length; j++) {
                  switch (response.results[0].address_components[i].types[j]) {
                    case "locality":
                      city = response.results[0].address_components[i].long_name;
                      break;
                    case "administrative_area_level_1":
                      state = response.results[0].address_components[i].long_name;
                      break;
                    case "country":
                      country = response.results[0].address_components[i].long_name;
                      break;
                  }
                }
              }
              console.log(city, state, country);
              console.log(address);
              setData(address)
            },
            (error) => {
              console.error(error);
            });
    }

    function error() {
        console.log("Unable to retrieve your location");
    }

    return (
        <div className="windows">
             <div className="containerInputLogin">
                <select className="select" name="medicalGroup" id="medicalGroup">
                    <option value="">Please select a medical group type</option>
                </select>
                <select name="speciality" id="speciality">
                    <option value="">Please select speciality type</option>
                </select>
                <div className='locationInput'>
                    <GooglePlacesAutocomplete 
                        apiKey={googleApiKey} 
                        selectProps={{
                            inputValue: data,
                            defaultInputValue: data, //set default value
                            onChange: setData, //save the value gotten from google
                            placeholder: "Select your localization",
                        }}
                    />
                    <button className='locButton' onClick={getLocation}><img className="locImage" src="loc.png"/></button>
                </div>
                <button className="btnSubmit" > Search </button>
            </div>
        </div>
    );
}

export default Home;