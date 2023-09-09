package org.groupscope.controller;

import org.groupscope.services.GroupScopeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GroupScopeControllerTest {

    @Mock
    private GroupScopeService groupScopeService;

    @InjectMocks
    private GroupScopeController groupScopeController;

    @Test
    void getSubjects() {

    }
}