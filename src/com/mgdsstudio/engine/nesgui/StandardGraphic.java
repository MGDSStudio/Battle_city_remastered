package com.mgdsstudio.engine.nesgui;

import io.itch.mgdsstudio.engine.libs.imagezones.ImageZoneSimpleData;

interface StandardGraphic {
    ImageZoneSimpleData MOVE_IMAGE_RIGHT = new ImageZoneSimpleData(65, 289+100, 98, 322+100);
    ImageZoneSimpleData MOVE_LEFT_SIDE_TO_RIGHT = new ImageZoneSimpleData(98, 389, 132, 422);
    ImageZoneSimpleData MOVE_LEFT_SIDE_TO_LEFT = new ImageZoneSimpleData(65, 389+33, 98, 422+33);
    ImageZoneSimpleData ZOOM_PLUS = new ImageZoneSimpleData(98, 389+33, 132, 422+33);
    ImageZoneSimpleData ZOOM_MINUS = new ImageZoneSimpleData(65, 389+33+33, 98, 422+33+33);
}
