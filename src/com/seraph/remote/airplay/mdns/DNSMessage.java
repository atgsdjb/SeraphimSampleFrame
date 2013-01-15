/*
 * Copyright 2011 David Simmons
 * http://cafbit.com/entry/testing_multicast_support_on_android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seraph.remote.airplay.mdns;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

/**
 * This class represents a single DNS message, and is capable
 * of parsing or constructing such a message.
 * 
 * see: http://www.ietf.org/rfc/rfc1035.txt
 * 
 * @author simmons
 */
public class DNSMessage implements Serializable  {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2671195195011175163L;

	private static short nextMessageId = 0;

    private short messageId;
    private ArrayList<DNSQuestion> questions = new ArrayList<DNSQuestion>();
    private ArrayList<DNSAnswer> answers = new ArrayList<DNSAnswer>();

    /**
     * Construct a DNS host query
     */
    public DNSMessage(String hostname) {
        messageId = nextMessageId++;
        questions.add(new DNSQuestion(DNSQuestion.DNSType.ANY, hostname));
    }
    
    public DNSMessage(DNSQuestion question){
    	messageId = 0;
    	questions.add(question);
    }
    /**
     * Parse the supplied packet as a DNS message.
     */
    public DNSMessage(byte[] packet) {
        parse(packet, 0, packet.length);
    }
    
    /**
     * Parse the supplied packet as a DNS message.
     */
    public DNSMessage(byte[] packet, int offset, int length) {
        parse(packet, offset, length);
    }
    
    public int length() {
        int length = 12; // header length
        for (DNSQuestion q : questions) {
            length += q.length();
        }
        for (DNSAnswer a : answers) {
            length += a.length();
        }
        return length;
    }
    
    public byte[] serialize() {
        DNSBuffer buffer = new DNSBuffer(length());
        
        // header
        buffer.writeShort(messageId);
        buffer.writeShort(0); // flags
        buffer.writeShort(questions.size()); // qdcount
        buffer.writeShort(answers.size()); // ancount
        buffer.writeShort(0); // nscount
        buffer.writeShort(0); // arcount
        
        // questions
        for (DNSQuestion question : questions) {
            question.serialize(buffer);
        }
        
        // answers
        for (DNSAnswer answer : answers) {
            answer.serialize(buffer);
        }
        
        return buffer.bytes;
    }
    
    private void parse(byte[] packet, int offset, int length) {
        DNSBuffer buffer = new DNSBuffer(packet, offset, length);
        
        // header
        messageId = buffer.readShort();
        buffer.readShort(); // flags
        int qdcount = buffer.readShort();
        int ancount = buffer.readShort();
        buffer.readShort(); // nscount
        buffer.readShort(); // arcount
        
        // questions
        questions.clear();
        for (int i=0; i<qdcount; i++) {
        	try{
        		questions.add(new DNSQuestion(buffer));
        	}catch(Exception e){
        		Log.e("DNSMessage","parse  Question occur"+e.getMessage());
        	}
        }
        
        // answers
        answers.clear();
        for (int i=0; i<ancount; i++) {
        	try{
        		answers.add( DNSAnswer.parse(buffer));
        	}catch (Exception e) {
				// TODO: handle exception
        		Log.e("DNSMessage","parse  Answer  occur"+e.getMessage());
			}
        }
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-----------------------------------------------------\n");
        sb.append("BEGIN   SHOW QUESTIONS\n");
        // questions
        for (DNSQuestion q : questions) {
            sb.append(q.toString()+"\n");
        }
        sb.append("END     SHOW QUESTION\n");
        sb.append("-----------------------------------------------------\n");
        sb.append("-----------------------------------------------------\n");
        /**
         * 3
         */
        String result = sb.toString();
        return result;
    }
    
    public List<DNSAnswer>  getAnswerList(){
    	return this.answers;
    }
    public List<DNSQuestion> getQuestionList(){
    	return this.questions;
    }
 
}
