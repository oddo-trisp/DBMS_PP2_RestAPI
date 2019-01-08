package gr.uoa.di.dbm.restapi.service;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import gr.uoa.di.dbm.restapi.entity.*;
import gr.uoa.di.dbm.restapi.repo.ServiceRequestRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ParserServiceImpl {
    private final ServiceRequestRepository serviceRequestRepository;
    private List<ServiceRequest> serviceRequests;
    private SimpleDateFormat dateFormat;

    private static final String GRAFFITY_REMOVAL = "Datasets/311-service-requests-graffiti-removal.csv";
    private static final String ABANDONED_BUILDING = "Datasets/311-service-requests-vacant-and-abandoned-buildings-reported.csv";
    private static final String ABANDONED_VEHICLE = "Datasets/311-service-requests-abandoned-vehicles.csv";
    private static final String GARBAGE_CART = "Datasets/311-service-requests-garbage-carts.csv";
    private static final String POT_HOLES_REPORTED = "Datasets/311-service-requests-pot-holes-reported.csv";
    private static final String RODENT_BAITING = "Datasets/311-service-requests-rodent-baiting.csv";
    private static final String SANITATION_CODE_COMPLAINT = "Datasets/311-service-requests-sanitation-code-complaints.csv";
    private static final String TREE_DEBRI = "Datasets/311-service-requests-tree-debris.csv";
    private static final String TRIM_TREE = "Datasets/311-service-requests-tree-trims.csv";
    private static final String ALLEY_LIGHTS_OUT = "Datasets/311-service-requests-alley-lights-out.csv";
    private static final String LIGHTS_OUT_ALL = "Datasets/311-service-requests-street-lights-all-out.csv";
    private static final String LIGHTS_OUT_ONE = "Datasets/311-service-requests-street-lights-one-out.csv";
    private static final int BATCH_SIZE = 100000;

    @Autowired
    public ParserServiceImpl(ServiceRequestRepository serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.serviceRequests = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }


    public void parseData(){
        graffityRemovalParser();
        abandonedBuildingsParser();
        abandonedVehiclesParser();
        garbageCartsParser();
        potHolesReportedParser();
        rodentBaitingParser();
        sanitationCodeComplaintsParser();
        treeDebrisParser();
        trimTreesParser();
        alleyLightsOutParser();
        streetLightsOutAllParser();
        streetLightsOutOneParser();
    }

    private void graffityRemovalParser(){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(GRAFFITY_REMOVAL));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {

                // Accessing values by Header names
                GraffityRemoval graffityRemoval = new GraffityRemoval();
                Location location = new Location();

                location.setCoordinates(getCoordinates(csvRecord,"X Coordinate", "Y Coordinate" ));
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(!StringUtils.isEmpty(csvRecord.get("Latitude")) ? Double.parseDouble(csvRecord.get("Latitude")) : null);
                location.setLongitude(!StringUtils.isEmpty(csvRecord.get("Longitude")) ? Double.parseDouble(csvRecord.get("Longitude")) : null);
                location.setLocationJson(csvRecord.get("Location"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(csvRecord.get("Street Address"));
                location.setWard(csvRecord.get("Ward"));
                location.setSsa(csvRecord.get("SSA"));
                location.setZipCode(csvRecord.get("ZIP Code"));

                graffityRemoval.setCompletionDate(!StringUtils.isEmpty(csvRecord.get("Completion Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()) : null);
                graffityRemoval.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()) : null);
                graffityRemoval.setServiceRequestNo(csvRecord.get("Service Request Number"));
                graffityRemoval.setStatus(csvRecord.get("Status"));
                graffityRemoval.setRequestType(csvRecord.get("Type of Service Request"));
                graffityRemoval.setSurface(csvRecord.get("What Type of Surface is the Graffiti on?"));
                graffityRemoval.setGraffityLocation(csvRecord.get("Where is the Graffiti located?"));

                graffityRemoval.setLocation(location);

                serviceRequests.add(graffityRemoval);

                //Write on batch size
                saveOnBatchSize();
            }
            //Write rest data
            saveRestData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abandonedBuildingsParser(){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(ABANDONED_BUILDING));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {

                // Accessing values by Header names
                AbandonnedBuilding abandonnedBuilding = new AbandonnedBuilding();
                Location location = new Location();

                String address = csvRecord.get("ADDRESS STREET NUMBER") + " "
                        + csvRecord.get("ADDRESS STREET DIRECTION") + " "
                        + csvRecord.get("ADDRESS STREET NAME") + " "
                        + csvRecord.get("ADDRESS STREET SUFFIX");

                location.setCoordinates(getCoordinates(csvRecord,"X COORDINATE", "Y COORDINATE" ));
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(!StringUtils.isEmpty(csvRecord.get("LATITUDE")) ? Double.parseDouble(csvRecord.get("LATITUDE")) : null);
                location.setLongitude(!StringUtils.isEmpty(csvRecord.get("LONGITUDE")) ? Double.parseDouble(csvRecord.get("LONGITUDE")) : null);
                location.setLocationJson(csvRecord.get("Location"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(address);
                location.setWard(csvRecord.get("Ward"));
                location.setZipCode(csvRecord.get("ZIP CODE"));

                abandonnedBuilding.setCreateDate(!StringUtils.isEmpty(csvRecord.get("DATE SERVICE REQUEST WAS RECEIVED")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("DATE SERVICE REQUEST WAS RECEIVED")).getTime()) : null);
                abandonnedBuilding.setServiceRequestNo(csvRecord.get("SERVICE REQUEST NUMBER"));
                abandonnedBuilding.setRequestType(csvRecord.get("SERVICE REQUEST TYPE"));
                abandonnedBuilding.setBuildingLocationOnTheLot(csvRecord.get("LOCATION OF BUILDING ON THE LOT (IF GARAGE, CHANGE TYPE CODE TO BGD)."));
                abandonnedBuilding.setBuildingDangerous(Boolean.valueOf(csvRecord.get("IS THE BUILDING DANGEROUS OR HAZARDOUS?")));
                abandonnedBuilding.setBuildingOpen(csvRecord.get("IS BUILDING OPEN OR BOARDED?"));
                abandonnedBuilding.setBuildingEntrance(csvRecord.get("IF THE BUILDING IS OPEN, WHERE IS THE ENTRY POINT?"));
                abandonnedBuilding.setBuildingVacant(csvRecord.get("IS THE BUILDING CURRENTLY VACANT OR OCCUPIED?"));
                abandonnedBuilding.setBuildingFire(Boolean.valueOf(csvRecord.get("IS THE BUILDING VACANT DUE TO FIRE?")));
                abandonnedBuilding.setBuildingUsage(Boolean.valueOf(csvRecord.get("ANY PEOPLE USING PROPERTY? (HOMELESS, CHILDEN, GANGS)")));

                abandonnedBuilding.setLocation(location);

                serviceRequests.add(abandonnedBuilding);

                //Write on batch size
               saveOnBatchSize();
            }

            //Write rest data
            saveRestData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abandonedVehiclesParser(){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(ABANDONED_VEHICLE));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {

                // Accessing values by Header names
                AbandonnedVehicle abandonnedVehicle = new AbandonnedVehicle();
                Location location = new Location();

                location.setCoordinates(getCoordinates(csvRecord,"X Coordinate", "Y Coordinate" ));
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(!StringUtils.isEmpty(csvRecord.get("Latitude")) ? Double.parseDouble(csvRecord.get("Latitude")) : null);
                location.setLongitude(!StringUtils.isEmpty(csvRecord.get("Longitude")) ? Double.parseDouble(csvRecord.get("Longitude")) : null);
                location.setLocationJson(csvRecord.get("Location"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(csvRecord.get("Street Address"));
                location.setWard(csvRecord.get("Ward"));
                location.setSsa(csvRecord.get("SSA"));
                location.setZipCode(csvRecord.get("ZIP Code"));

                abandonnedVehicle.setCompletionDate(!StringUtils.isEmpty(csvRecord.get("Completion Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()) : null);
                abandonnedVehicle.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()) : null);
                abandonnedVehicle.setServiceRequestNo(csvRecord.get("Service Request Number"));
                abandonnedVehicle.setStatus(csvRecord.get("Status"));
                abandonnedVehicle.setRequestType(csvRecord.get("Type of Service Request"));
                abandonnedVehicle.setCurrentActivity(csvRecord.get("Current Activity"));
                abandonnedVehicle.setMostRecentAction(csvRecord.get("Most Recent Action"));
                abandonnedVehicle.setDaysParked(!StringUtils.isEmpty(csvRecord.get("How Many Days Has the Vehicle Been Reported as Parked?"))
                        ? Double.parseDouble(csvRecord.get("How Many Days Has the Vehicle Been Reported as Parked?")) : null);
                abandonnedVehicle.setLicensePlate(csvRecord.get("License Plate"));
                abandonnedVehicle.setVehicleColor(csvRecord.get("Vehicle Color"));
                abandonnedVehicle.setVehicleModel(csvRecord.get("Vehicle Make/Model"));

                abandonnedVehicle.setLocation(location);

                serviceRequests.add(abandonnedVehicle);

                //Write on batch size
                saveOnBatchSize();
            }

            //Write rest data
           saveRestData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void garbageCartsParser(){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(GARBAGE_CART));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {

                // Accessing values by Header names
                GarbageCart garbageCart = new GarbageCart();
                Location location = new Location();

                location.setCoordinates(getCoordinates(csvRecord,"X Coordinate", "Y Coordinate" ));
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(!StringUtils.isEmpty(csvRecord.get("Latitude")) ? Double.parseDouble(csvRecord.get("Latitude")) : null);
                location.setLongitude(!StringUtils.isEmpty(csvRecord.get("Longitude")) ? Double.parseDouble(csvRecord.get("Longitude")) : null);
                location.setLocationJson(csvRecord.get("Location"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(csvRecord.get("Street Address"));
                location.setWard(csvRecord.get("Ward"));
                location.setSsa(csvRecord.get("SSA"));
                location.setZipCode(csvRecord.get("ZIP Code"));

                garbageCart.setCompletionDate(!StringUtils.isEmpty(csvRecord.get("Completion Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()) : null);
                garbageCart.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()) : null);
                garbageCart.setServiceRequestNo(csvRecord.get("Service Request Number"));
                garbageCart.setStatus(csvRecord.get("Status"));
                garbageCart.setRequestType(csvRecord.get("Type of Service Request"));
                garbageCart.setCurrentActivity(csvRecord.get("Current Activity"));
                garbageCart.setMostRecentAction(csvRecord.get("Most Recent Action"));
                garbageCart.setCartsDelivered(!StringUtils.isEmpty(csvRecord.get("Number of Black Carts Delivered"))
                        ? Double.valueOf(csvRecord.get("Number of Black Carts Delivered")).longValue() : null);

                garbageCart.setLocation(location);

                serviceRequests.add(garbageCart);

                //Write on batch size
                saveOnBatchSize();
            }

            //Write rest data
            saveRestData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void potHolesReportedParser(){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(POT_HOLES_REPORTED));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {

                // Accessing values by Header names
                PotHolesReported potHolesReported = new PotHolesReported();
                Location location = new Location();

                location.setCoordinates(getCoordinates(csvRecord,"X COORDINATE", "Y COORDINATE" ));
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(!StringUtils.isEmpty(csvRecord.get("LATITUDE")) ? Double.parseDouble(csvRecord.get("LATITUDE")) : null);
                location.setLongitude(!StringUtils.isEmpty(csvRecord.get("LONGITUDE")) ? Double.parseDouble(csvRecord.get("LONGITUDE")) : null);
                location.setLocationJson(csvRecord.get("LOCATION"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(csvRecord.get("STREET ADDRESS"));
                location.setWard(csvRecord.get("Ward"));
                location.setSsa(csvRecord.get("SSA"));
                location.setZipCode(csvRecord.get("ZIP"));

                potHolesReported.setCompletionDate(!StringUtils.isEmpty(csvRecord.get("COMPLETION DATE")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("COMPLETION DATE")).getTime()) : null);
                potHolesReported.setCreateDate(!StringUtils.isEmpty(csvRecord.get("CREATION DATE")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("CREATION DATE")).getTime()) : null);
                potHolesReported.setServiceRequestNo(csvRecord.get("SERVICE REQUEST NUMBER"));
                potHolesReported.setStatus(csvRecord.get("STATUS"));
                potHolesReported.setRequestType(csvRecord.get("TYPE OF SERVICE REQUEST"));
                potHolesReported.setCurrentActivity(csvRecord.get("CURRENT ACTIVITY"));
                potHolesReported.setMostRecentAction(csvRecord.get("MOST RECENT ACTION"));
                potHolesReported.setHolesNum(!StringUtils.isEmpty(csvRecord.get("NUMBER OF POTHOLES FILLED ON BLOCK"))
                        ? Double.valueOf(csvRecord.get("NUMBER OF POTHOLES FILLED ON BLOCK")).longValue() : null);

                potHolesReported.setLocation(location);

                serviceRequests.add(potHolesReported);

                //Write on batch size
                saveOnBatchSize();
            }

            //Write rest data
            saveRestData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rodentBaitingParser(){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(RODENT_BAITING));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {

                // Accessing values by Header names
                RodentBaiting rodentBaiting = new RodentBaiting();
                Location location = new Location();

                location.setCoordinates(getCoordinates(csvRecord,"X Coordinate", "Y Coordinate" ));
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(!StringUtils.isEmpty(csvRecord.get("Latitude")) ? Double.parseDouble(csvRecord.get("Latitude")) : null);
                location.setLongitude(!StringUtils.isEmpty(csvRecord.get("Longitude")) ? Double.parseDouble(csvRecord.get("Longitude")) : null);
                location.setLocationJson(csvRecord.get("Location"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(csvRecord.get("Street Address"));
                location.setWard(csvRecord.get("Ward"));
                location.setZipCode(csvRecord.get("ZIP Code"));

                rodentBaiting.setCompletionDate(!StringUtils.isEmpty(csvRecord.get("Completion Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()) : null);
                rodentBaiting.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()) : null);
                rodentBaiting.setServiceRequestNo(csvRecord.get("Service Request Number"));
                rodentBaiting.setStatus(csvRecord.get("Status"));
                rodentBaiting.setRequestType(csvRecord.get("Type of Service Request"));
                rodentBaiting.setCurrentActivity(csvRecord.get("Current Activity"));
                rodentBaiting.setMostRecentAction(csvRecord.get("Most Recent Action"));
                rodentBaiting.setBaitedNum(!StringUtils.isEmpty(csvRecord.get("Number of Premises Baited"))
                        ? Double.valueOf(csvRecord.get("Number of Premises Baited")).longValue() : null);
                rodentBaiting.setGarbageNum(!StringUtils.isEmpty(csvRecord.get("Number of Premises with Garbage"))
                        ? Double.valueOf(csvRecord.get("Number of Premises with Garbage")).longValue() : null);
                rodentBaiting.setGarbageNum(!StringUtils.isEmpty(csvRecord.get("Number of Premises with Rats"))
                        ? Double.valueOf(csvRecord.get("Number of Premises with Rats")).longValue() : null);

                rodentBaiting.setLocation(location);

                serviceRequests.add(rodentBaiting);

                //Write on batch size
                saveOnBatchSize();
            }

            //Write rest data
            saveRestData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sanitationCodeComplaintsParser(){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(SANITATION_CODE_COMPLAINT));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {

                // Accessing values by Header names
                SanitationCodeComplaint sanitationCodeComplaint = new SanitationCodeComplaint();
                Location location = new Location();

                location.setCoordinates(getCoordinates(csvRecord,"X Coordinate", "Y Coordinate" ));
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(!StringUtils.isEmpty(csvRecord.get("Latitude")) ? Double.parseDouble(csvRecord.get("Latitude")) : null);
                location.setLongitude(!StringUtils.isEmpty(csvRecord.get("Longitude")) ? Double.parseDouble(csvRecord.get("Longitude")) : null);
                location.setLocationJson(csvRecord.get("Location"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(csvRecord.get("Street Address"));
                location.setWard(csvRecord.get("Ward"));
                location.setZipCode(csvRecord.get("ZIP Code"));

                sanitationCodeComplaint.setCompletionDate(!StringUtils.isEmpty(csvRecord.get("Completion Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()) : null);
                sanitationCodeComplaint.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()) : null);
                sanitationCodeComplaint.setServiceRequestNo(csvRecord.get("Service Request Number"));
                sanitationCodeComplaint.setStatus(csvRecord.get("Status"));
                sanitationCodeComplaint.setRequestType(csvRecord.get("Type of Service Request"));
                sanitationCodeComplaint.setNatureViolation(csvRecord.get("What is the Nature of this Code Violation?"));

                sanitationCodeComplaint.setLocation(location);

                serviceRequests.add(sanitationCodeComplaint);

                //Write on batch size
                saveOnBatchSize();
            }

            //Write rest data
            saveRestData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void treeDebrisParser(){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(TREE_DEBRI));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {

                // Accessing values by Header names
                TreeDebri treeDebri = new TreeDebri();
                Location location = new Location();

                location.setCoordinates(getCoordinates(csvRecord,"X Coordinate", "Y Coordinate" ));
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(!StringUtils.isEmpty(csvRecord.get("Latitude")) ? Double.parseDouble(csvRecord.get("Latitude")) : null);
                location.setLongitude(!StringUtils.isEmpty(csvRecord.get("Longitude")) ? Double.parseDouble(csvRecord.get("Longitude")) : null);
                location.setLocationJson(csvRecord.get("Location"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(csvRecord.get("Street Address"));
                location.setWard(csvRecord.get("Ward"));
                location.setZipCode(csvRecord.get("ZIP Code"));

                treeDebri.setCompletionDate(!StringUtils.isEmpty(csvRecord.get("Completion Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()) : null);
                treeDebri.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()) : null);
                treeDebri.setServiceRequestNo(csvRecord.get("Service Request Number"));
                treeDebri.setStatus(csvRecord.get("Status"));
                treeDebri.setRequestType(csvRecord.get("Type of Service Request"));
                treeDebri.setCurrentActivity(csvRecord.get("Current Activity"));
                treeDebri.setMostRecentAction(csvRecord.get("Most Recent Action"));
                treeDebri.setDebrisLocation(csvRecord.get("If Yes, where is the debris located?"));

                treeDebri.setLocation(location);

                serviceRequests.add(treeDebri);

                //Write on batch size
                saveOnBatchSize();
            }

            //Write rest data
            saveRestData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void trimTreesParser(){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(TRIM_TREE));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {

                // Accessing values by Header names
                TrimTree trimTree = new TrimTree();
                Location location = new Location();

                location.setCoordinates(getCoordinates(csvRecord,"X Coordinate", "Y Coordinate" ));
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(!StringUtils.isEmpty(csvRecord.get("Latitude")) ? Double.parseDouble(csvRecord.get("Latitude")) : null);
                location.setLongitude(!StringUtils.isEmpty(csvRecord.get("Longitude")) ? Double.parseDouble(csvRecord.get("Longitude")) : null);
                location.setLocationJson(csvRecord.get("Location"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(csvRecord.get("Street Address"));
                location.setWard(csvRecord.get("Ward"));
                location.setZipCode(csvRecord.get("ZIP Code"));

                trimTree.setCompletionDate(!StringUtils.isEmpty(csvRecord.get("Completion Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()) : null);
                trimTree.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()) : null);
                trimTree.setServiceRequestNo(csvRecord.get("Service Request Number"));
                trimTree.setStatus(csvRecord.get("Status"));
                trimTree.setRequestType(csvRecord.get("Type of Service Request"));
                trimTree.setTreesLocation(csvRecord.get("Location of Trees"));

                trimTree.setLocation(location);

                serviceRequests.add(trimTree);

                //Write on batch size
                saveOnBatchSize();
            }

            //Write rest data
            saveRestData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alleyLightsOutParser(){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(ALLEY_LIGHTS_OUT));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {

                // Accessing values by Header names
                AlleyLightsOut alleyLightsOut = new AlleyLightsOut();
                Location location = new Location();

                location.setCoordinates(getCoordinates(csvRecord,"X Coordinate", "Y Coordinate" ));
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(!StringUtils.isEmpty(csvRecord.get("Latitude")) ? Double.parseDouble(csvRecord.get("Latitude")) : null);
                location.setLongitude(!StringUtils.isEmpty(csvRecord.get("Longitude")) ? Double.parseDouble(csvRecord.get("Longitude")) : null);
                location.setLocationJson(csvRecord.get("Location"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(csvRecord.get("Street Address"));
                location.setWard(csvRecord.get("Ward"));
                location.setZipCode(csvRecord.get("ZIP Code"));

                alleyLightsOut.setCompletionDate(!StringUtils.isEmpty(csvRecord.get("Completion Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()) : null);
                alleyLightsOut.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()) : null);
                alleyLightsOut.setServiceRequestNo(csvRecord.get("Service Request Number"));
                alleyLightsOut.setStatus(csvRecord.get("Status"));
                alleyLightsOut.setRequestType(csvRecord.get("Type of Service Request"));
                alleyLightsOut.setLocation(location);

                serviceRequests.add(alleyLightsOut);

                //Write on batch size
                saveOnBatchSize();
            }

            //Write rest data
            saveRestData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void streetLightsOutAllParser(){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(LIGHTS_OUT_ALL));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {

                // Accessing values by Header names
                LightsOutAll lightsOutAll = new LightsOutAll();
                Location location = new Location();

                location.setCoordinates(getCoordinates(csvRecord,"X Coordinate", "Y Coordinate" ));
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(!StringUtils.isEmpty(csvRecord.get("Latitude")) ? Double.parseDouble(csvRecord.get("Latitude")) : null);
                location.setLongitude(!StringUtils.isEmpty(csvRecord.get("Longitude")) ? Double.parseDouble(csvRecord.get("Longitude")) : null);
                location.setLocationJson(csvRecord.get("Location"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(csvRecord.get("Street Address"));
                location.setWard(csvRecord.get("Ward"));
                location.setZipCode(csvRecord.get("ZIP Code"));

                lightsOutAll.setCompletionDate(!StringUtils.isEmpty(csvRecord.get("Completion Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()) : null);
                lightsOutAll.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()) : null);
                lightsOutAll.setServiceRequestNo(csvRecord.get("Service Request Number"));
                lightsOutAll.setStatus(csvRecord.get("Status"));
                lightsOutAll.setRequestType(csvRecord.get("Type of Service Request"));
                lightsOutAll.setLocation(location);

                serviceRequests.add(lightsOutAll);

                //Write on batch size
                saveOnBatchSize();
            }

            //Write rest data
            saveRestData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void streetLightsOutOneParser(){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(LIGHTS_OUT_ONE));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {

                // Accessing values by Header names
                LightsOutOne lightsOutOne = new LightsOutOne();
                Location location = new Location();

                location.setCoordinates(getCoordinates(csvRecord,"X Coordinate", "Y Coordinate" ));
                location.setCommunityArea(csvRecord.get("Community Area"));
                location.setLatitude(!StringUtils.isEmpty(csvRecord.get("Latitude")) ? Double.parseDouble(csvRecord.get("Latitude")) : null);
                location.setLongitude(!StringUtils.isEmpty(csvRecord.get("Longitude")) ? Double.parseDouble(csvRecord.get("Longitude")) : null);
                location.setLocationJson(csvRecord.get("Location"));
                location.setPoliceDistrict(csvRecord.get("Police District"));
                location.setAddress(csvRecord.get("Street Address"));
                location.setWard(csvRecord.get("Ward"));
                location.setZipCode(csvRecord.get("ZIP Code"));

                lightsOutOne.setCompletionDate(!StringUtils.isEmpty(csvRecord.get("Completion Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()) : null);
                lightsOutOne.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()) : null);
                lightsOutOne.setServiceRequestNo(csvRecord.get("Service Request Number"));
                lightsOutOne.setStatus(csvRecord.get("Status"));
                lightsOutOne.setRequestType(csvRecord.get("Type of Service Request"));
                lightsOutOne.setLocation(location);

                serviceRequests.add(lightsOutOne);

                //Write on batch size
                saveOnBatchSize();
            }

            //Write rest data
            saveRestData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Point getCoordinates(CSVRecord csvRecord, String x, String y){
        String xCoord = csvRecord.get(x);
        String yCoord = csvRecord.get(y);
        return !StringUtils.isEmpty(xCoord) && !StringUtils.isEmpty(yCoord)
                ? new Point(new Position(Arrays.asList(
                Double.parseDouble(xCoord),
                Double.parseDouble(yCoord))))
                : null;
    }

    private void saveOnBatchSize(){
        if(serviceRequests.size() == BATCH_SIZE) {
            serviceRequestRepository.saveAll(serviceRequests);
            serviceRequests.clear();
        }
    }

    private void saveRestData(){
        serviceRequestRepository.saveAll(serviceRequests);
        serviceRequests.clear();
    }
}
