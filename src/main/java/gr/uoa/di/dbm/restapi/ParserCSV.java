package gr.uoa.di.dbm.restapi;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import gr.uoa.di.dbm.restapi.entity.GraffityRemoval;
import gr.uoa.di.dbm.restapi.entity.LightsOutOne;
import gr.uoa.di.dbm.restapi.entity.Location;
import gr.uoa.di.dbm.restapi.entity.ServiceRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;


public class ParserCSV {

    private static final String LIGHTS_OUT_ONE = "Datasets/311-service-requests-street-lights-one-out.csv";
    private static final String GRAFFITY_REMOVAL = "Datasets/311-service-requests-graffiti-removal.csv";

    public List<ServiceRequest> parseData(List<ServiceRequest> serviceRequests){
        serviceRequests = streetLightsOutOneParser(serviceRequests);
        serviceRequests = graffityRemovalParser(serviceRequests);
        return serviceRequests;
    }

    public List<ServiceRequest> streetLightsOutOneParser(List<ServiceRequest> serviceRequests){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(LIGHTS_OUT_ONE));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

                // Accessing values by Header names
                ServiceRequest serviceRequest = new LightsOutOne();
                Location location = new Location();
                List<Double> coordinatesList = Arrays.asList(Double.parseDouble(csvRecord.get("X Coordinate")),
                        Double.parseDouble(csvRecord.get("Y Coordinate")));
                Point coordinates = new Point(new Position(coordinatesList));

                location.setCoordinates(coordinates);
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(Double.parseDouble(csvRecord.get("Latitude")));
                location.setLongitude(Double.parseDouble(csvRecord.get("Longitude")));
                location.setLocationJson(csvRecord.get("Location"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(csvRecord.get("Street Address"));
                location.setWard(csvRecord.get("Ward"));
                location.setZipCode(csvRecord.get("ZIP Code"));

                serviceRequest.setCompletionDate(new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()));
                serviceRequest.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()));
                serviceRequest.setServiceRequestNo(csvRecord.get("Service Request Number"));
                serviceRequest.setStatus(csvRecord.get("Status"));
                serviceRequest.setRequestType(csvRecord.get("Type of Service Request"));
                serviceRequest.setLocation(location);

                if(isFirst) {
                    serviceRequests.add(serviceRequest);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return serviceRequests;
    }

    public List<ServiceRequest> graffityRemovalParser(List<ServiceRequest> serviceRequests){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(GRAFFITY_REMOVAL));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

                // Accessing values by Header names
                ServiceRequest serviceRequest = new GraffityRemoval();
                Location location = new Location();
                List<Double> coordinatesList = Arrays.asList(Double.parseDouble(csvRecord.get("X Coordinate")),
                        Double.parseDouble(csvRecord.get("Y Coordinate")));
                Point coordinates = new Point(new Position(coordinatesList));

                location.setCoordinates(coordinates);
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(Double.parseDouble(csvRecord.get("Latitude")));
                location.setLongitude(Double.parseDouble(csvRecord.get("Longitude")));
                location.setLocationJson(csvRecord.get("Location"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(csvRecord.get("Street Address"));
                location.setWard(csvRecord.get("Ward"));
                location.setSsa(csvRecord.get("SSA"));
                location.setZipCode(csvRecord.get("ZIP Code"));

                serviceRequest.setCompletionDate(new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()));
                serviceRequest.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()));
                serviceRequest.setServiceRequestNo(csvRecord.get("Service Request Number"));
                serviceRequest.setStatus(csvRecord.get("Status"));
                serviceRequest.setRequestType(csvRecord.get("Type of Service Request"));
                ((GraffityRemoval) serviceRequest).setSurface(csvRecord.get("What Type of Surface is the Graffiti on?"));
                ((GraffityRemoval) serviceRequest).setSurface(csvRecord.get("Where is the Graffiti located?"));

                serviceRequest.setLocation(location);

                if(isFirst) {
                    serviceRequests.add(serviceRequest);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return serviceRequests;
    }

}
