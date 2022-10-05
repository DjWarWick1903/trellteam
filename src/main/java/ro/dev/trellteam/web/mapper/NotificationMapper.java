package ro.dev.trellteam.web.mapper;

import org.mapstruct.Mapper;
import ro.dev.trellteam.domain.Notification;
import ro.dev.trellteam.web.dto.NotificationDto;

@Mapper
public interface NotificationMapper {
    Notification dtoToDomain(NotificationDto notificationDto);
    NotificationDto domainToDto(Notification notification);
}
