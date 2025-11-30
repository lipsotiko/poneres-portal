const getFile = async (f) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onloadend = () => {
      resolve({
        dataURL: reader.result,
        fileName: f.name,
      });
    };
    reader.readAsDataURL(f);
  });
};

export { getFile };
