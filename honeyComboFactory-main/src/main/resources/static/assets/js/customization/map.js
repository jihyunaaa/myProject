let map;
let userMarker;
let currentInfowindow = null;
let currentUserPosition = null;
let nearbyMarkers = []; // 주변 마커 배열
const currentBrand = document.body.dataset.brand; // jsp에서 받아온 브랜드
console.log("지도용 브랜드 : [" + currentBrand + "]");

function initMap() {
	map = new kakao.maps.Map(document.getElementById('mapWrapper'), {
		center: new kakao.maps.LatLng(37.5665, 126.9780),
		level: 3
	});
}

function getDistance(pos1, pos2) {
	const R = 6371e3;
	const lat1 = pos1.getLat() * Math.PI / 180;
	const lat2 = pos2.getLat() * Math.PI / 180;
	const deltaLat = (pos2.getLat() - pos1.getLat()) * Math.PI / 180;
	const deltaLng = (pos2.getLng() - pos1.getLng()) * Math.PI / 180;

	const a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
		Math.cos(lat1) * Math.cos(lat2) *
		Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);

	const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	return R * c;
}

function getLocation() {
	if (!navigator.geolocation) {
		printSweetAlert("warning", "브라우저가 위치정보 제공을 지원하지 않습니다.");
		return;
	}

	navigator.geolocation.getCurrentPosition(
		function(position) {
			const pos = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude);
			currentUserPosition = pos;
			map.setCenter(pos);

			if (userMarker) userMarker.setMap(null);

			const imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png';
			const imageSize = new kakao.maps.Size(24, 35);
			const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

			userMarker = new kakao.maps.Marker({
				position: pos,
				map: map,
				title: '내 위치',
				image: markerImage
			});
			searchNearby(pos);
		},
		function(error) {
			switch (error.code) {
				case error.PERMISSION_DENIED:
					printSweetAlert("warning", "위치 권한이 거부되었습니다. 브라우저 설정에서 위치 권한을 허용해 주세요.");
					break;
				case error.POSITION_UNAVAILABLE:
					printSweetAlert("warning", "위치 정보를 사용할 수 없습니다.");
					break;
				case error.TIMEOUT:
					printSweetAlert("warning", "위치 정보 요청이 시간 초과되었습니다.");
					break;
				default:
					printSweetAlert("warning", "알 수 없는 위치 오류가 발생했습니다.");
					break;
			}

			// 예외 발생 시 fallback 기본 위치 설정 (서울역)
			const fallback = new kakao.maps.LatLng(37.554722, 126.970833); // 서울역
			currentUserPosition = fallback;
			map.setCenter(fallback);

			if (userMarker) userMarker.setMap(null);

			const imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png';
			const imageSize = new kakao.maps.Size(24, 35);
			const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

			userMarker = new kakao.maps.Marker({
				position: fallback,
				map: map,
				title: '기본 위치 (서울역)',
				image: markerImage
			});
			searchNearby(fallback);
		},
		{
			enableHighAccuracy: true, // 고정밀 모드
			timeout: 10000,           // 10초 타임아웃
			maximumAge: 0             // 캐시된 위치 허용 안 함
		}
	);
}


function clearNearbyMarkers() {
	nearbyMarkers.forEach(m => m.setMap(null));
	nearbyMarkers = [];
}

function searchStore() {
	const keyword = document.getElementById('searchStoreKeyword').value.trim();
	console.log("편의점 검색어 [", keyword, "]");

	if (!keyword) {
		printSweetAlert("warning", "편의점 이름을 입력해주세요.");
		return;
	}

	if (!currentUserPosition) {
		printSweetAlert("warning", "현재 위치 정보를 먼저 가져와 주세요.");
		return;
	}

	const ps = new kakao.maps.services.Places();
	ps.keywordSearch(keyword, function(data, status) {
		if (status === kakao.maps.services.Status.OK && data.length > 0) {
			const brandRegex = new RegExp(`^${currentBrand}\\b`);
			const filtered = data.filter(place =>
				currentBrand === 'COMBO' || brandRegex.test(place.place_name)
			);

			if (filtered.length === 0) {
				printSweetAlert("warning", currentBrand + " 편의점 중 해당 이름을 찾을 수 없습니다.");
				return;
			}

			let closestPlace = filtered[0];
			let minDistance = getDistance(currentUserPosition, new kakao.maps.LatLng(closestPlace.y, closestPlace.x));

			filtered.forEach(place => {
				const dist = getDistance(currentUserPosition, new kakao.maps.LatLng(place.y, place.x));
				if (dist < minDistance) {
					minDistance = dist;
					closestPlace = place;
				}
			});

			const latlng = new kakao.maps.LatLng(closestPlace.y, closestPlace.x);
			map.setCenter(latlng);

			if (currentInfowindow) currentInfowindow.close();

			const marker = new kakao.maps.Marker({
				map: map,
				position: latlng
			});

			const infowindow = new kakao.maps.InfoWindow({
				content: '<div style="padding:5px;font-size:12px;">'
					+ closestPlace.place_name + '<br>'
					+ (closestPlace.road_address_name || closestPlace.address_name)
					+ '</div>'
			});
			infowindow.open(map, marker);
			currentInfowindow = infowindow;

			kakao.maps.event.addListener(marker, 'click', function() {
				if (currentInfowindow) currentInfowindow.close();
				infowindow.open(map, marker);
				currentInfowindow = infowindow;
			});
		} else {
			printSweetAlert("warning", "해당 편의점을 찾을 수 없습니다.");
		}
	}, {
		location: currentUserPosition,
		radius: 3000
	});
}

function searchNearby(location) {
	clearNearbyMarkers();
	const ps = new kakao.maps.services.Places();

	ps.categorySearch('CS2', function(result, status) {
		if (status === kakao.maps.services.Status.OK) {
			result.forEach(function(place) {
				if (
					(typeof currentBrand !== 'undefined') &&
					(
						(currentBrand === 'CU' && place.place_name.includes('CU')) ||
						(currentBrand === 'GS25' && place.place_name.includes('GS25')) ||
						(currentBrand === 'COMBO')
					)
				) {
					displayMarker(place);
				}
			});
		} else {
			printSweetAlert("warning", "주변에 편의점을 찾을 수 없습니다.");
		}
	}, {
		location: location,
		radius: 300
	});
}

function displayMarker(place) {
	const coords = new kakao.maps.LatLng(place.y, place.x);

	const marker = new kakao.maps.Marker({
		map: map,
		position: coords
	});
	nearbyMarkers.push(marker);

	const infowindow = new kakao.maps.InfoWindow({
		content: '<div style="padding:5px;font-size:12px;">'
			+ place.place_name + '<br>'
			+ (place.road_address_name || place.address_name)
			+ '</div>'
	});

	kakao.maps.event.addListener(marker, 'click', function() {
		if (currentInfowindow) currentInfowindow.close();
		infowindow.open(map, marker);
		currentInfowindow = infowindow;
	});
}

kakao.maps.load(function() {
	initMap();
	getLocation();
});

$(document).ready(function() {
	$(document).on("keydown", "#searchStoreKeyword", function(event) {
		if (event.key === "Enter" || event.keyCode === 13) {
			console.log("주변 편의점 검색 엔터키 실행");
			searchStore();
		}
	});
});
