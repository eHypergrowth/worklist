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

        String msg = "MSH|^~\\&|BROKER|TINTEGRO|DCM4CHEE|TINTEGRO|||OMI^O23|12345|P|2.5.1\r"
                + "PID|||C8F26D5||PACIENTE N DE^PRUEBA||1990505|M\r"
                + "PV1||||||||1234^CMP123|||||||A0||||908831\r"
                + "ORC|NW|12359|12359||SC\r"
                + "TQ1|||||||202107081930||R\r"
                + "OBR||||||||||||||||1234^CMP123||||||||||||||||||||||||||||ABDOMEN TOTAL SIMPLE\r"
                + "IPC|12359|CODPROCEDIMIENTO|12359|CODPROCEDIMIENTO|CT||CT111185||111185\r";

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

        } catch (HL7Exception ex) {
            Logger.getLogger(prueba.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LLPException ex) {
            Logger.getLogger(prueba.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(prueba.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            connection.close();
        }

    }

}
