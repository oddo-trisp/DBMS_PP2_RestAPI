package gr.uoa.di.dbm.restapi.service;

import gr.uoa.di.dbm.restapi.entity.*;
import gr.uoa.di.dbm.restapi.repo.ServiceRequestRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class ParserServiceImpl {
    private final ServiceRequestRepository serviceRequestRepository;
    private List<ServiceRequest> serviceRequests;
    private List<Citizen> citizens;
    private SimpleDateFormat dateFormat;
    private int upvoteCounter;

    private static final String CITIZENS = "Datasets/citizen_file.csv";
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
    private static final int UPVOTE_LIMIT = 3000000;

    @Autowired
    public ParserServiceImpl(ServiceRequestRepository serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.serviceRequests = new ArrayList<>();
        this.citizens = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        this.upvoteCounter = 0;
    }


    public void parseData() {
        parseCitizens();
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

    private void parseCitizens() {
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(CITIZENS));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            for (CSVRecord csvRecord : csvParser) {
                Citizen citizen = new Citizen();
                citizen.setName(csvRecord.get("name"));
                citizen.setBirthday(csvRecord.get("birthday"));
                citizen.setOccupation(csvRecord.get("occupation"));
                citizen.setPhone(csvRecord.get("phone"));
                citizen.setEmail(csvRecord.get("email"));
                citizen.setVotes(0);

                citizens.add(citizen);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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
                        dateFormat.parse(csvRecord.get("Completion Date")) : null);
                graffityRemoval.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        dateFormat.parse(csvRecord.get("Creation Date")) : null);
                graffityRemoval.setServiceRequestNo(csvRecord.get("Service Request Number"));
                graffityRemoval.setStatus(csvRecord.get("Status"));
                graffityRemoval.setRequestType(csvRecord.get("Type of Service Request"));
                graffityRemoval.setSurface(csvRecord.get("What Type of Surface is the Graffiti on?"));
                graffityRemoval.setGraffityLocation(csvRecord.get("Where is the Graffiti located?"));

                graffityRemoval.setLocation(location);

                addUpvoters(graffityRemoval);

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
                        dateFormat.parse(csvRecord.get("DATE SERVICE REQUEST WAS RECEIVED")) : null);
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

                addUpvoters(abandonnedBuilding);

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
                        dateFormat.parse(csvRecord.get("Completion Date")) : null);
                abandonnedVehicle.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        dateFormat.parse(csvRecord.get("Creation Date")) : null);
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

                addUpvoters(abandonnedVehicle);

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
                        dateFormat.parse(csvRecord.get("Completion Date")) : null);
                garbageCart.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        dateFormat.parse(csvRecord.get("Creation Date")) : null);
                garbageCart.setServiceRequestNo(csvRecord.get("Service Request Number"));
                garbageCart.setStatus(csvRecord.get("Status"));
                garbageCart.setRequestType(csvRecord.get("Type of Service Request"));
                garbageCart.setCurrentActivity(csvRecord.get("Current Activity"));
                garbageCart.setMostRecentAction(csvRecord.get("Most Recent Action"));
                garbageCart.setCartsDelivered(!StringUtils.isEmpty(csvRecord.get("Number of Black Carts Delivered"))
                        ? Double.valueOf(csvRecord.get("Number of Black Carts Delivered")).longValue() : null);

                garbageCart.setLocation(location);

                addUpvoters(garbageCart);

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
                        dateFormat.parse(csvRecord.get("COMPLETION DATE")) : null);
                potHolesReported.setCreateDate(!StringUtils.isEmpty(csvRecord.get("CREATION DATE")) ?
                        dateFormat.parse(csvRecord.get("CREATION DATE")) : null);
                potHolesReported.setServiceRequestNo(csvRecord.get("SERVICE REQUEST NUMBER"));
                potHolesReported.setStatus(csvRecord.get("STATUS"));
                potHolesReported.setRequestType(csvRecord.get("TYPE OF SERVICE REQUEST"));
                potHolesReported.setCurrentActivity(csvRecord.get("CURRENT ACTIVITY"));
                potHolesReported.setMostRecentAction(csvRecord.get("MOST RECENT ACTION"));
                potHolesReported.setHolesNum(!StringUtils.isEmpty(csvRecord.get("NUMBER OF POTHOLES FILLED ON BLOCK"))
                        ? Double.valueOf(csvRecord.get("NUMBER OF POTHOLES FILLED ON BLOCK")).longValue() : null);

                potHolesReported.setLocation(location);

                addUpvoters(potHolesReported);

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
                        dateFormat.parse(csvRecord.get("Completion Date")) : null);
                rodentBaiting.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        dateFormat.parse(csvRecord.get("Creation Date")) : null);
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

                addUpvoters(rodentBaiting);

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
                        dateFormat.parse(csvRecord.get("Completion Date")) : null);
                sanitationCodeComplaint.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        dateFormat.parse(csvRecord.get("Creation Date")) : null);
                sanitationCodeComplaint.setServiceRequestNo(csvRecord.get("Service Request Number"));
                sanitationCodeComplaint.setStatus(csvRecord.get("Status"));
                sanitationCodeComplaint.setRequestType(csvRecord.get("Type of Service Request"));
                sanitationCodeComplaint.setNatureViolation(csvRecord.get("What is the Nature of this Code Violation?"));

                sanitationCodeComplaint.setLocation(location);

                addUpvoters(sanitationCodeComplaint);

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
                        dateFormat.parse(csvRecord.get("Completion Date")) : null);
                treeDebri.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        dateFormat.parse(csvRecord.get("Creation Date")) : null);
                treeDebri.setServiceRequestNo(csvRecord.get("Service Request Number"));
                treeDebri.setStatus(csvRecord.get("Status"));
                treeDebri.setRequestType(csvRecord.get("Type of Service Request"));
                treeDebri.setCurrentActivity(csvRecord.get("Current Activity"));
                treeDebri.setMostRecentAction(csvRecord.get("Most Recent Action"));
                treeDebri.setDebrisLocation(csvRecord.get("If Yes, where is the debris located?"));

                treeDebri.setLocation(location);

                addUpvoters(treeDebri);

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
                        dateFormat.parse(csvRecord.get("Completion Date")) : null);
                trimTree.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        dateFormat.parse(csvRecord.get("Creation Date")) : null);
                trimTree.setServiceRequestNo(csvRecord.get("Service Request Number"));
                trimTree.setStatus(csvRecord.get("Status"));
                trimTree.setRequestType(csvRecord.get("Type of Service Request"));
                trimTree.setTreesLocation(csvRecord.get("Location of Trees"));

                trimTree.setLocation(location);

                addUpvoters(trimTree);

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
                        dateFormat.parse(csvRecord.get("Completion Date")) : null);
                alleyLightsOut.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        dateFormat.parse(csvRecord.get("Creation Date")) : null);
                alleyLightsOut.setServiceRequestNo(csvRecord.get("Service Request Number"));
                alleyLightsOut.setStatus(csvRecord.get("Status"));
                alleyLightsOut.setRequestType(csvRecord.get("Type of Service Request"));
                alleyLightsOut.setLocation(location);

                addUpvoters(alleyLightsOut);

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
                        dateFormat.parse(csvRecord.get("Completion Date")) : null);
                lightsOutAll.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        dateFormat.parse(csvRecord.get("Creation Date")) : null);
                lightsOutAll.setServiceRequestNo(csvRecord.get("Service Request Number"));
                lightsOutAll.setStatus(csvRecord.get("Status"));
                lightsOutAll.setRequestType(csvRecord.get("Type of Service Request"));
                lightsOutAll.setLocation(location);

                addUpvoters(lightsOutAll);

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
                        dateFormat.parse(csvRecord.get("Completion Date")) : null);
                lightsOutOne.setCreateDate(!StringUtils.isEmpty(csvRecord.get("Creation Date")) ?
                        dateFormat.parse(csvRecord.get("Creation Date")) : null);
                lightsOutOne.setServiceRequestNo(csvRecord.get("Service Request Number"));
                lightsOutOne.setStatus(csvRecord.get("Status"));
                lightsOutOne.setRequestType(csvRecord.get("Type of Service Request"));
                lightsOutOne.setLocation(location);

                addUpvoters(lightsOutOne);

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
                ? new Point(Double.parseDouble(xCoord), Double.parseDouble(yCoord))
                : null;
    }

    private void addUpvoters(ServiceRequest serviceRequest){
        if(randomInt(0,1) == 1 && upvoteCounter < UPVOTE_LIMIT){
            int listRealEnd = citizens.size();
            int listStart = randomInt(0,listRealEnd);
            int possibleEnd = randomInt(listStart, listStart+49);
            int listEnd = possibleEnd <= listRealEnd ? possibleEnd : listRealEnd;      //Max 1000 upvoters

            List<Citizen> votes = citizens.subList(listStart,listEnd)
                    .stream()
                    .filter(c -> c.getVotes() < 1000)
                    .peek(c -> c.setVotes(c.getVotes()+1))
                    .collect(Collectors.toList());

            serviceRequest.setUpvotes(votes);

            upvoteCounter++;
        }
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

    private int randomInt(int min, int max){
        return (int) Math.ceil(ThreadLocalRandom.current().nextInt(min, max+1));
    }
}
