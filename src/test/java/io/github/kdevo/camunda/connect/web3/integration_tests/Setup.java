package io.github.kdevo.camunda.connect.web3.integration_tests;

public class Setup {
    public static final String ENDPOINT = "http://192.168.42.42:7545";

    public enum FHAChainMember {
        FH("0x6781C0139403dCD57b5DAd0fe1BA86Bd8e66dcE1"),
        PROF("0xd62BEA6D6Cac82e8945a99cDaaC9f0c46D711947"),
        STUDENT1("0x8B426B3cedc4C1adAc2086FEFD6cDE426541913B"),
        STUDENT2("0x23090a27F02716dB82bd3034b9fA85A15aE4222C"),
        GUEST("0x65f4295918880c333e3b2337795ef2e94dddaf0f");

        private String address;

        FHAChainMember(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }
    }

    public enum Contract {
        MEMBER_STORAGE("0x3c0c84fff050feac2b16b7ac031825ff8bae0ac4"),
        EXAMINATION_SERVICE("0x445498852a156e62f652f720f6be5f7819524a34");

        private String address;

        Contract(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }
    }
}
