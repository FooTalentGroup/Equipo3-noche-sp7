const config = {
  theme: {
    extend: {
      colors: {
        "btn-primary": "oklch(var(--btn-primary))",
      },
      fontFamily: {
        "roboto-flex": ['"Roboto Flex"'], // Define a custom utility class
      },
      maxWidth: {
        '1086': '1086px',
        '1116': '1116px',
        '700': '700px',
        '600': '600px',
        '450': '450px',
        '410': '410px',
        '384': '384px',
        '432': '432px',
      },
      maxHeight: {
        '673': '673px',
        '700': '700px',
        '720': '720px',
        '460': '460px',
        '450': '450px',
        '420': '420px',
        '400': '400px',
        '500': '500px',
      },
      width: {
        '700': '700px',
        '199': '199px',
        '177-25': '177.25px',
        '72-25': '72.25px',
        '33': '33px',
        '450': '450px',
      },
      height: {
        '46': '46px',
        '18': '18px',
      },
      minHeight: {
        '500': '500px',
      },
      zIndex: {
        '1000': '1000',
        '2000': '2000',
      },
    },
  },
};

export default config;

