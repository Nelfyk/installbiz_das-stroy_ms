package ru.installbiz.das_stroy.entity;

import ru.moysklad.remap_1_2.entities.agents.Organization;
import ru.moysklad.remap_1_2.entities.documents.positions.InvoiceDocumentPosition;

@lombok.Data
public class Data {
    private String id;
    private String accountId;
    private long sum;
    private Organization organization;
    private InvoiceDocumentPosition positions;
}
