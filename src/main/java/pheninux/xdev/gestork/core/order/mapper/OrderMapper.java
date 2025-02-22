package pheninux.xdev.gestork.core.order.mapper;


import pheninux.xdev.gestork.core.dish.mapper.DishMapper;
import pheninux.xdev.gestork.core.order.model.OrderEntity;
import pheninux.xdev.gestork.core.order.model.dto.OrderEntityDto;

public class OrderMapper {

    public static OrderEntityDto toDto(OrderEntity orderEntity) {
        if (orderEntity == null) {
            return null; // ou vous pouvez lancer une exception selon votre besoin
        }

        OrderEntityDto orderDto = new OrderEntityDto();
        orderDto.setOrderId(orderEntity.getOrderId());
        orderDto.setCustomerLogin(orderEntity.getCustomerLogin());
        orderDto.setWaiterId(orderEntity.getWaiter() != null ? orderEntity.getWaiter().getEmployeeId() : null);
        orderDto.setTableNumber(orderEntity.getTableNumber());
        orderDto.setTotalAmount(orderEntity.getTotalAmount());
        orderDto.setOrderDate(orderEntity.getOrderDate());
        orderDto.setOrderType(orderEntity.getOrderType() != null ? orderEntity.getOrderType().name() : null);
        orderDto.setOrderDetailsId(orderEntity.getOrderDetails() != null ? orderEntity.getOrderDetails().getOrderDetailsId() : null);
        orderDto.setOrderedDishes(DishMapper.toDtoList(orderEntity.getOrderDetails().getOrderedDishes()));
        return orderDto;
    }

    public static OrderEntity toEntity(OrderEntityDto orderDto) {
        if (orderDto == null) {
            return null;
        }

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(orderDto.getOrderId());
        orderEntity.setCustomerLogin(orderDto.getCustomerLogin());
        // Vous devrez récupérer l'objet Employee par son ID si nécessaire
        // orderEntity.setWaiter(yourEmployeeService.findById(orderDto.getWaiterId()));
        orderEntity.setTableNumber(orderDto.getTableNumber());
        orderEntity.setTotalAmount(orderDto.getTotalAmount());
        orderEntity.setOrderDate(orderDto.getOrderDate());
        // Vous devrez gérer la conversion de OrderType ici si c'est un enum
        // orderEntity.setOrderType(OrderType.valueOf(orderDto.getOrderType()));
        return orderEntity;
    }
}