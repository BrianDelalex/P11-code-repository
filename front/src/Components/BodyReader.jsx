const BodyReader = async (body) => {

    const utf8Decoder = new TextDecoder("utf-8");
    const reader = body.getReader();
    let result = "";

    await reader.read().then(function processText({ done, value }) {
        if (done) {
            return;
        }
        result += utf8Decoder.decode(value, {stream: true});
        return reader.read().then(processText);
    });
    return result;
}

export default BodyReader;