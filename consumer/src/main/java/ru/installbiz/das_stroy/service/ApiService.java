package ru.installbiz.das_stroy.service;

import org.apache.commons.io.FileUtils;
import ru.installbiz.das_stroy.entity.Config;
import ru.moysklad.remap_1_2.ApiClient;
import ru.moysklad.remap_1_2.clients.EntityClient;
import ru.moysklad.remap_1_2.entities.Attribute;
import ru.moysklad.remap_1_2.entities.Meta;
import ru.moysklad.remap_1_2.entities.Template;
import ru.moysklad.remap_1_2.entities.documents.InvoiceOut;
import ru.moysklad.remap_1_2.utils.ApiClientException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

public class ApiService {
    private InvoiceOut invoiceOut;
    private ApiClient api;

    private ApiClient getApiClient() {
        return new ApiClient("online.moysklad.ru", true, Config.getAPI_LOGIN(), Config.getAPI_PASSWORD());
    }

    public void build(String id) throws ApiClientException, IOException {
        authenticator();
        api = getApiClient();
        EntityClient apiEntity = api.entity();
        invoiceOut = apiEntity.invoiceout().get(id);
        Config.setPositionSize(invoiceOut.getPositions().getMeta().getSize());
        Config.setName(invoiceOut.getName());
        getXls();

    }

    private void getXls() throws ApiClientException, IOException {
        List<Template> templateList = api.entity().invoiceout().metadata().customtemplate().getRows();
        for (Template template : templateList)
            if (template.getName().equals(Config.TEMPLATE_NAME)) {

                api.entity().invoiceout().export(Config.getId(), template, new File(Config.PATH + Config.TEMPLATE_NAME + "-" + Config.getName() + Config.FORMAT));
                URL url = new URL(template.getContent());

                try (InputStream in = url.openStream();
                     ReadableByteChannel rbc = Channels.newChannel(in);
                     FileOutputStream fos = new FileOutputStream(Config.PATH + Config.TEMPLATE_NAME + Config.FORMAT)) {
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                }
            }

    }

    public void insertLink(String linkValue) throws ApiClientException, IOException {
        List<Attribute> metaAttributeList = api.entity().invoiceout().metadataAttributes().getRows();
        for (Attribute a : metaAttributeList)
            if (a.getName().equals(Config.LINK_NAME)) {
                List<Attribute> attributeList = invoiceOut.getAttributes();
                attributeList.add(new Attribute(Meta.Type.INVOICE_OUT, a.getId(), Attribute.Type.linkValue, linkValue));
                invoiceOut.setAttributes(attributeList);
                api.entity().invoiceout().update(invoiceOut);
                break;
            }
    }

    public void deleteTempFiles() throws IOException {
        FileUtils.cleanDirectory(new File(Config.PATH));
    }

    private void authenticator() {
        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.getAPI_LOGIN(), Config.getAPI_PASSWORD().toCharArray());
            }
        });
    }
}