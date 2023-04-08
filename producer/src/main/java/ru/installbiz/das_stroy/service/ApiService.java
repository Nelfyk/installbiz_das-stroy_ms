package ru.installbiz.das_stroy.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import ru.installbiz.das_stroy.entity.Config;
import ru.moysklad.remap_1_2.ApiClient;
import ru.moysklad.remap_1_2.entities.Attribute;
import ru.moysklad.remap_1_2.entities.documents.InvoiceOut;
import ru.moysklad.remap_1_2.responses.ListEntity;
import ru.moysklad.remap_1_2.utils.ApiClientException;
import ru.moysklad.remap_1_2.utils.params.ApiParam;

import java.io.IOException;
import java.util.List;

public class ApiService {
    private ApiClient getApiClient() {
        return new ApiClient("online.moysklad.ru", true, Config.getAPI_LOGIN(), Config.getAPI_PASSWORD());
    }

    public void poll() throws ApiClientException, IOException {
        ApiClient api = getApiClient();
        ListEntity<InvoiceOut> invoiceOutList = api.entity().invoiceout().get(new ApiParam(ApiParam.Type.limit) {
            @Override
            protected String render(String host) {
                return "100";
            }
        }, new ApiParam(ApiParam.Type.order) {
            @Override
            protected String render(String host) {
                return "updated,desc";
            }
        });
        for (InvoiceOut i : invoiceOutList.getRows()) {
//            System.out.println(i.getUpdated());
            if (i.getAttributes().get(0).getValue().equals(true)) {
                sendInQueue(i.getId()); // Отправить в очередь
                changeTag(api, i);      // Убрать галку с флажка
            }
        }
    }

    private void sendInQueue(String id) {
        final String REGION = "ru-central1";
        final String ENDPOINT = "https://message-queue.api.cloud.yandex.net/";

        AwsClientBuilder.EndpointConfiguration ep = new AwsClientBuilder.EndpointConfiguration(ENDPOINT, REGION);
        final AmazonSQS sqs = AmazonSQSClientBuilder.standard().withEndpointConfiguration(ep).build();
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(Config.getQUEUE_URL())
                .withMessageBody(id);
        sqs.sendMessage(send_msg_request);
    }

    private void changeTag(ApiClient api, InvoiceOut invoiceout) throws ApiClientException, IOException {
        List<Attribute> attributeList = invoiceout.getAttributes();
        for (Attribute a : attributeList) {
            if (a.getName().equals(Config.ATTRIBUTE_NAME) && a.getValue().equals(true)) {
                a.setValue(false);
                break;
            }
        }
        invoiceout.setAttributes(attributeList);
        api.entity().invoiceout().update(invoiceout);
    }
}