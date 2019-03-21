import PermissionsTypes from "forpdi/jsx/planning/enum/PermissionsTypes.json";
import _ from 'underscore';


const permissionTypesByApp = {
	forpdi: [
		..._.map(PermissionsTypes, permission => {
			if (permission.match('forpdi')) {
				return permission;
			}
		})
	],
	forrisco: [
		..._.map(PermissionsTypes, permission => {
			if (permission.match('forrisco')) {
				return permission;
			}
		})
	],
}

export default permissionTypesByApp;
