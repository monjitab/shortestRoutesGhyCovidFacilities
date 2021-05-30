 
      const ghy= {
      
      north: 26.1992 ,
      south: 26.1064 ,
      west: 91.6632 ,
      east: 91.8376,
      
      };
      
      let map;
      let marker;
      let centre = { lat: 26.1433, lng: 91.7898 };
      let user = {};
      let destination = {};
      let facilities = [];
      let points = [];
      let route = [];
      let target;  
      let tcolour;
      let myroute; 
      let trafficLayer;
      
      function CenterControl(controlDiv, map) {
        
        const controlUI = document.createElement("div");
        controlUI.style.backgroundColor = "#fff";
        controlUI.style.border = "2px solid #fff";
        controlUI.style.borderRadius = "3px";
        controlUI.style.boxShadow = "0 2px 6px rgba(0,0,0,.3)";
        controlUI.style.cursor = "pointer";
        controlUI.style.marginTop = "8px";
        controlUI.style.marginBottom = "22px";
        controlUI.style.textAlign = "center";
        controlUI.title = "Click to recenter the map";
        controlDiv.appendChild(controlUI);
      
        const controlText = document.createElement("div");
        controlText.style.color = "rgb(25,25,25)";
        controlText.style.fontFamily = "Roboto,Arial,sans-serif";
        controlText.style.fontSize = "16px";
        controlText.style.lineHeight = "38px";
        controlText.style.paddingLeft = "5px";
        controlText.style.paddingRight = "5px";
        controlText.innerHTML = "Center Map";
        controlUI.appendChild(controlText);
        
        controlUI.addEventListener("click", () => {
          map.setCenter(centre);
        });
      }
      
      function initMap() {
      
          map = new google.maps.Map(document.getElementById("map"), {
          center:  centre,
          restriction : {
          latLngBounds : ghy,
          strictBounds : false,
          },
          zoom: 7,
          styles: [
            { elementType: "geometry", stylers: [{ color: "#242f3e" }] },
            {
              elementType: "labels.text.stroke",
              stylers: [{ color: "#242f3e" }],
            },
            {
              elementType: "labels.text.fill",
              stylers: [{ color: "#746855" }],
            },
            {
              featureType: "administrative.locality",
              elementType: "labels.text.fill",
              stylers: [{ color: "#d59563" }],
            },
            {
              featureType: "poi",
              elementType: "icon",
              stylers: [{ visibility: "off" }],
            },
            {
              featureType: "poi",
              elementType: "labels.text.fill",
              stylers: [{ color: "#d59563" }],
            },
            {
              featureType: "poi.park",
              elementType: "geometry",
              stylers: [{ color: "#263c3f" }],
            },
            {
              featureType: "poi.park",
              elementType: "labels.text.fill",
              stylers: [{ color: "#6b9a76" }],
            },
            {
              featureType: "road",
              elementType: "geometry",
              stylers: [{ color: "#38414e" }],
            },
            {
              featureType: "road",
              elementType: "geometry.stroke",
              stylers: [{ color: "#212a37" }],
            },
            {
              featureType: "road",
              elementType: "labels.text.fill",
              stylers: [{ color: "#9ca5b3" }],
            },
            {
              featureType: "road.highway",
              elementType: "geometry",
              stylers: [{ color: "#746855" }],
            },
            {
              featureType: "road.highway",
              elementType: "geometry.stroke",
              stylers: [{ color: "#1f2835" }],
            },
            {
              featureType: "road.highway",
              elementType: "labels.text.fill",
              stylers: [{ color: "#f3d19c" }],
            },
            {
              featureType: "transit",
              elementType: "geometry",
              stylers: [{ color: "#2f3948" }],
            },
            {
              featureType: "transit.station",
              elementType: "labels.text.fill",
              stylers: [{ color: "#d59563" }],
            },
            {
              featureType: "water",
              elementType: "geometry",
              stylers: [{ color: "#17263c" }],
            },
            {
              featureType: "water",
              elementType: "labels.text.fill",
              stylers: [{ color: "#515c6d" }],
            },
            {
              featureType: "water",
              elementType: "labels.text.stroke",
              stylers: [{ color: "#17263c" }],
            },
          ],
        });
        
        const card = document.getElementById("pac-card");
        const input = document.getElementById("pac-input");
        
        const options = {
          componentRestrictions: { country: "in" },
          fields: ["formatted_address", "geometry", "name"],
          origin: map.getCenter(),
          strictBounds: true,
          types: ["establishment"],
        };
        
        map.controls[google.maps.ControlPosition.TOP_RIGHT].push(card);
        const autocomplete = new google.maps.places.Autocomplete(
          input,
          options
        );
        
        // Bind the map's bounds (viewport) property to the autocomplete object, so that the autocomplete requests use the current map bounds for the
        // bounds option in the request.
        
        autocomplete.bindTo("bounds", map);
        
        marker = new google.maps.Marker({
          map,
          anchorPoint: new google.maps.Point(0, -29),
        });
        
        autocomplete.addListener("place_changed", () => {
         
          
        marker.setVisible(false);
        const place = autocomplete.getPlace();

        if (!place.geometry || !place.geometry.location) {
            // User entered the name of a Place that was not suggested and
            // pressed the Enter key, or the Place Details request failed.
            window.alert(
              "No details available for input: '" + place.name + "'"
            );
            return;
          }

          // If the place has a geometry, then present it on a map.
          
        map.setZoom(17);
        map.setCenter(place.geometry.location);
        marker.setPosition(place.geometry.location);
        marker.setVisible(true);
        marker.setDraggable(true);
        user = { lat: marker.getPosition().lat(), lng: marker.getPosition().lng() } ;
        centre = user;
                    
        });
       
       const centerControlDiv = document.createElement("div");
       CenterControl(centerControlDiv, map);
       map.controls[google.maps.ControlPosition.TOP_CENTER].push(
          centerControlDiv
        );
          
       google.maps.event.addListener(marker,"dragend", function() {
      
       user = { lat: marker.getPosition().lat(), lng: marker.getPosition().lng() } ; 
       centre = user;
             
          } );
      }
      
      function sendUserLoc ()
      {
         if(Object.keys(user).length === 0)
          { return ""; }
         
         marker.setDraggable(false);
         marker.setTitle("My Location");
         var latt = marker.getPosition().lat();
         var lngg = marker.getPosition().lng();
         user = { lat: latt, lng: lngg } ;
         centre = user;
         return latt+" "+lngg; 

      } 
     
     function zoomout()
     { 
         map.setZoom(7); 
     }
     
     function remuser()
     { 
         marker.setDraggable(true); 
     }
           
     function sendDestination ()
     {
         if(Object.keys(destination).length === 0)
          { return ""; }
          
         return destination.lat+" "+destination.lng;
     } 
        
      
         
      function showTraffic() {
      
        if(trafficLayer!=null)
        {trafficLayer.setMap(map);}
        
        else
        {trafficLayer = new google.maps.TrafficLayer();
        trafficLayer.setMap(map);}
      }
      
      function hideTraffic() {
        
        if(trafficLayer!=null)
         { trafficLayer.setMap(null); }
      }
        
      function remfacilities()
      { 
          for(let i = 0; i<facilities.length; i++) {
             facilities[i].setMap(null); } 
         facilities = [];
          
      }  
      
      function rempoints()
      {
         for(let i = 0; i<points.length; i++) {
             points[i].setMap(null); } 
         points = [];
          
      }  
      
      function addroute(location)
      {
         var arr = location.split(" ");
         var latt = parseFloat(arr[0]);
         var lang = parseFloat(arr[1]);
         
         var loc = { lat: latt , lng: lang} ;
         route.push(loc);
      }
      
      function newroute()
      {
         route = [];
         if(myroute!=null)
         {
           myroute.setMap(null); }
         
      } 
      
      function newDest()
      {  
         if(target!=null)
          { target.setIcon("http://maps.google.com/mapfiles/ms/icons/"+tcolour+"-dot.png"); } 
         destination = {};
         
      }    
        
        
      function drawroute()
      {
          myroute = new google.maps.Polyline( {
          path : route,
          geodesic : true,
          strokeColor : "#FF0000",
          strokeOpacity : 1.0,
          strokeWeight : 2,
          map : map,
         });
          
      }  
              
      function addfacility(latt,lang,colour,title)
      {
         var loc = { lat: latt , lng: lang} ;
         
         var m = new google.maps.Marker({
         position : loc,
         map : map,
         icon : "http://maps.google.com/mapfiles/ms/icons/"+colour+"-dot.png",
         });
        
         infoW = new google.maps.InfoWindow();
         
         google.maps.event.addListener(m,"mouseover", function() {
         infoW.setContent(title);
         infoW.open(map,this); });
         
         google.maps.event.addListener(m,"mouseout", function() {
         infoW.close(); });
         
         google.maps.event.addListener(m,"click", function() {
         
         destination = { lat: m.getPosition().lat(), lng: m.getPosition().lng() } ;
        
         if(target!=null)
          { target.setIcon("http://maps.google.com/mapfiles/ms/icons/"+tcolour+"-dot.png"); }
         target = m;
         tcolour = colour;
         m.setIcon("http://maps.google.com/mapfiles/kml/pal2/icon13.png");
        
          } );
          
         facilities.push(m);
  
      }
      
      function addpoint(location,color,delay)
      {
         var arr = location.split(" ");
         var latt = parseFloat(arr[0]);
         var lang = parseFloat(arr[1]);
         
         var loc = { lat: latt , lng: lang} ;
         
         var m = new google.maps.Marker({
         position : loc,
         map : null,
         icon : "http://maps.google.com/mapfiles/kml/pal4/icon60.png",
         });
         
         points.push(m);
        
         window.setTimeout(function() {
		  m.setMap(map);
         }, delay);
    
      }
      
      function addMarker(location, map) {
        
        new google.maps.Marker({
          position: location,
          map: map,
        });
      }