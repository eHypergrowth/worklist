/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.hapi;

/**
 *
 * @author alanm
 */

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class prueba {

    /**
     * Introducing the HAPI context
     */
    public static void main(String[] args) {

        //Creacion del mensaje 
        //Creación de la conexión con el metodo newClient, pasando host, puerto y useTLS
        //Creación del iniciador a partir de la conexión
        String host = "172.17.200.23";
        int port = 2575; // The port to listen on
        boolean useTls = false; // Should we use TLS/SSL?
        HapiContext context = new DefaultHapiContext();
        context.getParserConfiguration().setValidating(false);

        //MSH
        String aplicacionEnvia = "MWLJAVA"; //MAX 227
        String sendingFacility = "API"; //MAX 227
        String receivingApplication = "DCM4CHEE"; //MAX 227
        String receivingFacility = sendingFacility; //SAME
        String idMessageControl = "123456"; //MAX 20
        
        //PID
        String patientId = "AURA000616HCHGBLA6"; //MAX 250 va a ser la curp
        String nombrePaciente = "PACIENTE^FAKE"; //MAX 250
        String birthday = "20000616"; //MAX 26
        String sex = "F"; //MALE, FEMALE, AMBIGUOUS, NOT APPLICABLE, OTHER, UKNOW
        
        //ORC
        String control = "NW"; //NW = NUEVO, 2 DÍGITOS, 
        String iDNIfOptional = "11112"; //MAX 22
        
        //TQ1
        String dateTime = "202107151930"; //26 HORA INICIO
        String priority = "R"; //De rutina MAX 250
        
        //OBR
        String proveedor = "1234^CMP123"; //Creo que es opcional MAX 250 //Es el medico que refiere
        String procedure = "UN CR DE PRUEBA"; //PROCEDIMIENTO MAX 250
        
        //IPC
        String accessionNumber = "5"; //MAX 80
        String solProcedure = "CR PRUEB"; //MAX 22 //este es el nombre como aparece en el pacs requested
        String uidIdStudyInstance = "C8F4209"; //MAX 70
        String idStepProcedure = "CR PROC"; //MAX 22
        String modality = "CR"; //MAX 16
        String stationName = "FCR-CSL"; //MAX 22
        String scheduledAE = "FCR-CSL" ; //MAX 16
        
        String msg = "MSH|^~\\&|" + aplicacionEnvia + "|" + sendingFacility + "|" + receivingApplication+"|" + receivingFacility+ "|||OMI^O23|" + idMessageControl+ "|P|2.5.1\r"
                + "PID|||"+ patientId +"||" + nombrePaciente + "||" + birthday + "|" + sex+ "\r"
                //+ "PV1||||||2359||1234^CMP123|||||||A0||||908831\r" 
                + "ORC|" + control + "|" + iDNIfOptional+ "|" +iDNIfOptional + "||SC\r"
                + "TQ1|||||||" + dateTime + "||" + priority +"\r"
                + "OBR||||||||||||||||" + proveedor + "||||||||||||||||||||||||||||" + procedure +"\r"
                + "IPC|" + accessionNumber + "|" + solProcedure + "|" + uidIdStudyInstance + "|" + idStepProcedure + "|" + modality +"||" + stationName +"||" + scheduledAE +"\r";

        Parser p = context.getPipeParser();
        Message adt = null;
        Connection connection = null;
        try {
            adt = p.parse(msg); //el otro usa p.enconde
            // Remember, we created our HAPI Context above like so:
            // HapiContext context = new DefaultHapiContext();
            // A connection object represents a socket attached to an HL7 server
            connection = context.newClient(host, port, useTls);

            // The initiator is used to transmit unsolicited messages
            Initiator initiator = connection.getInitiator();
            Message response = null;

            response = initiator.sendAndReceive(adt);
            String responseString = p.encode(response);
            System.out.println("Received response:\n" + responseString);

        } catch (HL7Exception | LLPException | IOException  ex  ) {
            Logger.getLogger(prueba.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            connection.close();
        }
    }
}
