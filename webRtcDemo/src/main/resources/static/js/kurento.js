const ws = new WebSocket(`ws://${location.host}/kurento`);
const localVideoEl = document.getElementById('localVideo');

let localStream;
let pc;

const pcConfig = {
  iceServers: [
    {
      urls: 'stun:stun.l.google.com:19302'
    }
  ]
};

ws.onmessage = function (e) {
  const jsonData = JSON.parse(e.data);
  console.log(`[RECEIVE] ${JSON.stringify(jsonData)}`);

  switch (jsonData.id) {
    case 'icecandidate':
      pc.addIceCandidate(new RTCIceCandidate(jsonData.result)).then(() => console.log('addIceCandidate'));
      break;
    case 'presenterAnswer':
      if (jsonData.result === 'accepted') {
        pc.setRemoteDescription(new RTCSessionDescription({sdp: jsonData.sdpAnswer, type: "answer"})).then(() => {
          console.log('answer - setRemoteDescription');
        });
      }
      break;
    case 'viewerOffer':
      pc = createRTCPeerConnection(false);
      pc.createOffer().then(offer => {
        pc.setLocalDescription(offer).then(() => {
          const obj = {
            id: 'viewer',
            sdpOffer: offer
          }
          send(obj);
        });
      })
      break;
    case 'viewerAnswer':
      if (jsonData.result === 'accepted') {
        pc.setRemoteDescription(new RTCSessionDescription({sdp: jsonData.sdpAnswer, type: "answer"})).then(() => {
          console.log('answer - setRemoteDescription');
        });
      }
      break;
    default:
      console.log(jsonData);
      break;
  }
}

ws.onclose = function (e) {
  console.log(`[CLOSE] ${e.code} ${e.reason}`);
}

ws.onerror = function (e) {
  console.log(`[ERROR] ${e.message}`);
}

function createRTCPeerConnection(isPresenter = false) {
  const pc = new RTCPeerConnection(pcConfig);

  pc.addTransceiver('video', {direction: isPresenter ? 'sendonly' : 'recvonly'});
  pc.addTransceiver('audio', {direction: isPresenter ? 'sendonly' : 'recvonly'});

  try {
    pc.addEventListener('icecandidate', (e) => {
      if (e.candidate) {
        const obj = {
          id: 'icecandidate',
          result: e.candidate
        }
        send(obj);
        console.log('ICE candidate');
      }
    });

    pc.addEventListener('track', (e) => {
      if (isPresenter) { return; }
      localVideoEl.srcObject = e.streams[0];
    })

    pc.addEventListener('iceconnectionstatechange', (e) => {
      console.log(`iceConnectionState: ${e.target.iceConnectionState}`)
    });

    if (localStream !== undefined) {
      localStream.getTracks().forEach(track => {
        pc.addTrack(track, localStream);
      });
    }

    console.log('createRTCPeerConnection');
  } catch (e) {
    console.error(e);
  }
  return pc;
}

function send(msgObj) {
  const jsonData = JSON.stringify(msgObj);
  console.log(`[SEND] ${jsonData}`);
  ws.send(jsonData);
}

async function startBroadcast() {
  localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true } );
  localVideoEl.srcObject = localStream;

  pc = createRTCPeerConnection(true);
  pc.createOffer().then(offer => {
    pc.setLocalDescription(offer).then(() => {
      const obj = {
        id: 'presenter',
        sdpOffer: offer
      }
      send(obj);
    });
  })
}