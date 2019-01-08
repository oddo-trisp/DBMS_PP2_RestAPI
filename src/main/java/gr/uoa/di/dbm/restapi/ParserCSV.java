package gr.uoa.di.dbm.restapi;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import gr.uoa.di.dbm.restapi.entity.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.util.StringUtils;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ParserCSV {

    private List<ServiceRequest> serviceRequests = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

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

    public List<ServiceRequest> parseData(){
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

        return serviceRequests;
    }

    private void graffityRemovalParser(){
        try {

            Reader reader;
            reader = Files.newBufferedReader(Paths.get(GRAFFITY_REMOVAL));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

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

                graffityRemoval.setCompletionDate(new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()));
                graffityRemoval.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()));
                graffityRemoval.setServiceRequestNo(csvRecord.get("Service Request Number"));
                graffityRemoval.setStatus(csvRecord.get("Status"));
                graffityRemoval.setRequestType(csvRecord.get("Type of Service Request"));
                graffityRemoval.setSurface(csvRecord.get("What Type of Surface is the Graffiti on?"));
                graffityRemoval.setGraffityLocation(csvRecord.get("Where is the Graffiti located?"));

                graffityRemoval.setLocation(location);

                if(isFirst) {
                    serviceRequests.add(graffityRemoval);
                    break;
                }
            }

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

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

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

                abandonnedBuilding.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("DATE SERVICE REQUEST WAS RECEIVED")).getTime()));
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

                if(isFirst) {
                    serviceRequests.add(abandonnedBuilding);
                    break;
                }
            }

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

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

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

                abandonnedVehicle.setCompletionDate(new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()));
                abandonnedVehicle.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()));
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

                if(isFirst) {
                    serviceRequests.add(abandonnedVehicle);
                    break;
                }
            }

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

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

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

                potHolesReported.setCompletionDate(new Timestamp(dateFormat.parse(csvRecord.get("COMPLETION DATE")).getTime()));
                potHolesReported.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("CREATION DATE")).getTime()));
                potHolesReported.setServiceRequestNo(csvRecord.get("SERVICE REQUEST NUMBER"));
                potHolesReported.setStatus(csvRecord.get("STATUS"));
                potHolesReported.setRequestType(csvRecord.get("TYPE OF SERVICE REQUEST"));
                potHolesReported.setCurrentActivity(csvRecord.get("CURRENT ACTIVITY"));
                potHolesReported.setMostRecentAction(csvRecord.get("MOST RECENT ACTION"));
                potHolesReported.setHolesNum(!StringUtils.isEmpty(csvRecord.get("NUMBER OF POTHOLES FILLED ON BLOCK"))
                        ? Long.parseLong(csvRecord.get("NUMBER OF POTHOLES FILLED ON BLOCK")) : null);

                potHolesReported.setLocation(location);

                if(isFirst) {
                    serviceRequests.add(potHolesReported);
                    break;
                }
            }

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

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

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

                rodentBaiting.setCompletionDate(new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()));
                rodentBaiting.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()));
                rodentBaiting.setServiceRequestNo(csvRecord.get("Service Request Number"));
                rodentBaiting.setStatus(csvRecord.get("Status"));
                rodentBaiting.setRequestType(csvRecord.get("Type of Service Request"));
                rodentBaiting.setCurrentActivity(csvRecord.get("Current Activity"));
                rodentBaiting.setMostRecentAction(csvRecord.get("Most Recent Action"));
                rodentBaiting.setBaitedNum(!StringUtils.isEmpty(csvRecord.get("Number of Premises Baited"))
                        ? Long.parseLong(csvRecord.get("Number of Premises Baited")) : null);
                rodentBaiting.setGarbageNum(!StringUtils.isEmpty(csvRecord.get("Number of Premises with Garbage"))
                        ? Long.parseLong(csvRecord.get("Number of Premises with Garbage")) : null);
                rodentBaiting.setGarbageNum(!StringUtils.isEmpty(csvRecord.get("Number of Premises with Rats"))
                        ? Long.parseLong(csvRecord.get("Number of Premises with Rats")) : null);

                rodentBaiting.setLocation(location);

                if(isFirst) {
                    serviceRequests.add(rodentBaiting);
                    break;
                }
            }

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

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

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

                garbageCart.setCompletionDate(new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()));
                garbageCart.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()));
                garbageCart.setServiceRequestNo(csvRecord.get("Service Request Number"));
                garbageCart.setStatus(csvRecord.get("Status"));
                garbageCart.setRequestType(csvRecord.get("Type of Service Request"));
                garbageCart.setCurrentActivity(csvRecord.get("Current Activity"));
                garbageCart.setMostRecentAction(csvRecord.get("Most Recent Action"));
                garbageCart.setCartsDelivered(!StringUtils.isEmpty(csvRecord.get("Number of Black Carts Delivered"))
                        ? Long.parseLong(csvRecord.get("Number of Black Carts Delivered")) : null);

                garbageCart.setLocation(location);

                if(isFirst) {
                    serviceRequests.add(garbageCart);
                    break;
                }
            }

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

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

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

                sanitationCodeComplaint.setCompletionDate(new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()));
                sanitationCodeComplaint.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()));
                sanitationCodeComplaint.setServiceRequestNo(csvRecord.get("Service Request Number"));
                sanitationCodeComplaint.setStatus(csvRecord.get("Status"));
                sanitationCodeComplaint.setRequestType(csvRecord.get("Type of Service Request"));
                sanitationCodeComplaint.setNatureViolation(csvRecord.get("What is the Nature of this Code Violation?"));

                sanitationCodeComplaint.setLocation(location);

                if(isFirst) {
                    serviceRequests.add(sanitationCodeComplaint);
                    break;
                }
            }

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

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

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

                treeDebri.setCompletionDate(new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()));
                treeDebri.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()));
                treeDebri.setServiceRequestNo(csvRecord.get("Service Request Number"));
                treeDebri.setStatus(csvRecord.get("Status"));
                treeDebri.setRequestType(csvRecord.get("Type of Service Request"));
                treeDebri.setCurrentActivity(csvRecord.get("Current Activity"));
                treeDebri.setMostRecentAction(csvRecord.get("Most Recent Action"));
                treeDebri.setDebrisLocation(csvRecord.get("If Yes, where is the debris located?"));

                treeDebri.setLocation(location);

                if(isFirst) {
                    serviceRequests.add(treeDebri);
                    break;
                }
            }

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

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

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

                trimTree.setCompletionDate(new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()));
                trimTree.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()));
                trimTree.setServiceRequestNo(csvRecord.get("Service Request Number"));
                trimTree.setStatus(csvRecord.get("Status"));
                trimTree.setRequestType(csvRecord.get("Type of Service Request"));
                trimTree.setTreesLocation(csvRecord.get("Location of Trees"));

                trimTree.setLocation(location);

                if(isFirst) {
                    serviceRequests.add(trimTree);
                    break;
                }
            }

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

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

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

                alleyLightsOut.setCompletionDate(new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()));
                alleyLightsOut.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()));
                alleyLightsOut.setServiceRequestNo(csvRecord.get("Service Request Number"));
                alleyLightsOut.setStatus(csvRecord.get("Status"));
                alleyLightsOut.setRequestType(csvRecord.get("Type of Service Request"));
                alleyLightsOut.setLocation(location);

                if(isFirst) {
                    serviceRequests.add(alleyLightsOut);
                    break;
                }
            }

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

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

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

                lightsOutAll.setCompletionDate(new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()));
                lightsOutAll.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()));
                lightsOutAll.setServiceRequestNo(csvRecord.get("Service Request Number"));
                lightsOutAll.setStatus(csvRecord.get("Status"));
                lightsOutAll.setRequestType(csvRecord.get("Type of Service Request"));
                lightsOutAll.setLocation(location);

                if(isFirst) {
                    serviceRequests.add(lightsOutAll);
                    break;
                }
            }

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

            boolean isFirst = true;

            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord);

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

                lightsOutOne.setCompletionDate(new Timestamp(dateFormat.parse(csvRecord.get("Completion Date")).getTime()));
                lightsOutOne.setCreateDate(new Timestamp(dateFormat.parse(csvRecord.get("Creation Date")).getTime()));
                lightsOutOne.setServiceRequestNo(csvRecord.get("Service Request Number"));
                lightsOutOne.setStatus(csvRecord.get("Status"));
                lightsOutOne.setRequestType(csvRecord.get("Type of Service Request"));
                lightsOutOne.setLocation(location);

                if(isFirst) {
                    serviceRequests.add(lightsOutOne);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Point getCoordinates(CSVRecord csvRecord, String x, String y){
        String xCoord = csvRecord.get(x);
        String yCoord = csvRecord.get(y);
        Point coordinates = !StringUtils.isEmpty(xCoord) && !StringUtils.isEmpty(yCoord)
                ? new Point(new Position(Arrays.asList(
                Double.parseDouble(xCoord),
                Double.parseDouble(yCoord))))
                : null;
        return coordinates;
    }


}
