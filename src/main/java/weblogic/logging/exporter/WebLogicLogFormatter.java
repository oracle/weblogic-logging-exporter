package weblogic.logging.exporter;

import co.elastic.logging.jul.EcsFormatter;
import org.slf4j.MDC;
import weblogic.logging.WLLogRecord;

import java.util.logging.LogRecord;

public class WebLogicLogFormatter extends EcsFormatter {
    public static final String FIELDS_MESSAGE_ID = "fields.messageID";
    public static final String FIELDS_SERVER_NAME = "fields.serverName";
    public static final String FIELDS_USER_ID = "fields.userId";
    public static final String FIELDS_SUB_SYSTEM = "fields.subSystem";
    public static final String FIELDS_MACHINE_NAME = "fields.machineName";
    public static final String FIELDS_TRANSACTION_ID = "fields.transactionId";
    public static final String FIELDS_DIAGNOSTIC_CONTEXT_ID = "fields.diagnosticContextId";
    public static final String FIELDS_SEQUENCE_NUMBER = "fields.sequenceNumber";
    public static final String FIELDS_DOMAIN_UID = "fields.domainUID";

    private final String domainUID;

    public WebLogicLogFormatter(String domainUID) {
        this.domainUID = domainUID;
    }

    @Override
    public String format(final LogRecord record) {
        WLLogRecord wlLogRecord = (WLLogRecord) record;

        MDC.put(FIELDS_MESSAGE_ID, wlLogRecord.getId());
        MDC.put(FIELDS_SERVER_NAME, wlLogRecord.getServerName());
        MDC.put(FIELDS_USER_ID, wlLogRecord.getUserId());
        MDC.put(FIELDS_SUB_SYSTEM, wlLogRecord.getSubsystem());
        MDC.put(FIELDS_MACHINE_NAME, wlLogRecord.getMachineName());
        MDC.put(FIELDS_TRANSACTION_ID, wlLogRecord.getTransactionId());
        MDC.put(FIELDS_DIAGNOSTIC_CONTEXT_ID, wlLogRecord.getDiagnosticContextId());
        MDC.put(FIELDS_SEQUENCE_NUMBER, String.valueOf(wlLogRecord.getSequenceNumber()));
        MDC.put(FIELDS_DOMAIN_UID, domainUID);

        String result = super.format(wlLogRecord);

        // Can't clear the whole MDC HashMap as there might be other records in there.
        MDC.remove(FIELDS_MESSAGE_ID);
        MDC.remove(FIELDS_SERVER_NAME);
        MDC.remove(FIELDS_USER_ID);
        MDC.remove(FIELDS_SUB_SYSTEM);
        MDC.remove(FIELDS_MACHINE_NAME);
        MDC.remove(FIELDS_TRANSACTION_ID);
        MDC.remove(FIELDS_DIAGNOSTIC_CONTEXT_ID);
        MDC.remove(FIELDS_SEQUENCE_NUMBER);
        MDC.remove(FIELDS_DOMAIN_UID);

        return result;
    }
}
