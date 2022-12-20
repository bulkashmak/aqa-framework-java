package uz.gateway.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.gateway.GatewayClient;
import uz.gateway.GatewayContext;

@Service
@Slf4j
public class UsersService extends GatewayClient {

    private final GatewayContext context = new GatewayContext();
}
