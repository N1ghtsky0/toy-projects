const ws = new WebSocket(`ws://${location.host}/webrtc`);
const pcListMap = new Map();
const localVideoEl = document.getElementById('localVideo');
let otherKeyList = [];

let socketId = null;
let localStream;

ws.onmessage = function (e) {
  const jsonData = JSON.parse(e.data);
  console.log(`[RECEIVE] ${JSON.stringify(jsonData)}`);

  let key = jsonData.key;
  switch (jsonData.id) {
    case 'join':
      socketId = key;
      break;
    case 'viewerList':
      otherKeyList = jsonData.result;
      otherKeyList.forEach(key => {
        if (socketId !== key && !pcListMap.has(key)) {
          pcListMap.set(key, createRTCPeerConnection(key));
          createOffer(pcListMap.get(key), key);
        }
      })
      break;
    case 'icecandidate':
      pcListMap.get(key).addIceCandidate(new RTCIceCandidate(jsonData.result)).then(() => console.log('addIceCandidate'));
      break;
    case 'offer':
      let offer = jsonData.result;
      pcListMap.set(key, createRTCPeerConnection(key));
      pcListMap.get(key).setRemoteDescription(new RTCSessionDescription(offer)).then(() => {
        console.log('offer - setRemoteDescription');
        createAnswer(pcListMap.get(key), key);
      });
      break;
    case 'answer':
      let answer = jsonData.result;
      pcListMap.get(key).setRemoteDescription(new RTCSessionDescription(answer)).then(() => {
        console.log('answer - setRemoteDescription');
      });
      break;
    case 'broadcastStop':
      clear();
      break;
    default:
      console.log(jsonData);
      break;
  }
}

function createRTCPeerConnection(key) {
  const pc = new RTCPeerConnection();

  try {
    pc.addEventListener('icecandidate', (e) => {
      if (e.candidate) {
        const obj = {
          id: 'icecandidate',
          to: key,
          result: e.candidate
        }
        send(obj);
        console.log('ICE candidate');
      }
    });

    pc.addEventListener('track', (e) => {
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

function createOffer(pc, key) {
  pc.createOffer().then(offer => {
    pc.setLocalDescription(offer).then(() => {
      const obj = {
        id: 'offer',
        to: key,
        result: offer
      }
      send(obj);
    });
  })
}

function createAnswer(pc, key) {
  pc.createAnswer().then(answer => {
    pc.setLocalDescription(answer).then(() => {
      const obj = {
        id: 'answer',
        to: key,
        result: answer
      }
      send(obj);
    })
  })
}

function send(msgObj) {
  const jsonData = JSON.stringify(msgObj);
  console.log(`[SEND] ${jsonData}`);
  ws.send(jsonData);
}

async function startBroadcast() {
  localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true } );
  localVideoEl.srcObject = localStream;
  send({
    id: 'startBroadcast',
  })
}

function stopBroadcast() {
  send({
    id: 'stopBroadcast',
  })
}

function clear() {
  localStream = null;
  localVideoEl.srcObject = null;
  pcListMap.forEach(pc => {
    pc.close();
  })
  pcListMap.clear();
}