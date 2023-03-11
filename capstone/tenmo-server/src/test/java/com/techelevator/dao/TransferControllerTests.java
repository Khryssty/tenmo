package com.techelevator.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techelevator.tenmo.controller.TransferController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TransferControllerTests {


    @Autowired
    TransferController controller;

    @Autowired
    ObjectMapper mapper;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        System.out.println("setup()...");
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void sendTransferShouldCreateNewTransfer() throws Exception {

    }
}
