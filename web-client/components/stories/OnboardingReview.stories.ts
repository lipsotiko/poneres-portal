import type { Meta, StoryObj } from "@nuxtjs/storybook";

import OnboardingReview from "../OnboardingReview.vue";

const meta = {
  component: OnboardingReview,
  title: "Components/OnboardingReview",
} satisfies Meta<typeof OnboardingReview>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Primary: Story = {
  args: {
    values: {
      licenseFiles: [
        {
          license: {
            name: 'md-license.pdf'
          },
          state: "MD",
          licenseNumber: "123456789",
          expirationDate: "2029-01-02",
        },
      ],
      educationDetails: [
        {
          schoolName: "Towson University",
          graduationDate: "2017-12-13",
          degree: "BS",
        },
        {
          schoolName: "Drexel University",
          graduationDate: "2020-12-13",
          degree: "MD",
        },
      ],
      employmentHistory: [
        {
          employerName: "General Dynamics",
          startDate: "2020-03-02",
          endDate: "2023-12-13",
          positionDetails:
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
          supervisorName: "Jane Smith",
          supervisorContact: "444-333-1111",
        },
        {
          employerName: "Pivotal Labs",
          startDate: "2023-12-03",
          endDate: "2024-6-11",
          positionDetails:
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
          supervisorName: "Jack Jones",
          supervisorContact: "444-333-2222",
        },
      ],
      schedulePreferences: ["day", "evening"],
      firstName: "Elias",
      middleName: "Sevastianos",
      lastName: "Poneres",
      gender: "Male",
      dob: "2007-01-02",
      citizenshipStatus: "US Citizen",
      specialty: "Anesthesiology",
      npi: "123456789",
      location: "MD",
      homeAddress: {
        addressLine1: "111 Beech Ave.",
        city: "Baltimore",
        state: "MD",
        zip: "21211",
      },
      mailingAddress: {
        addressLine1: "222 Beech Ave.",
        city: "Baltimore",
        state: "MD",
        zip: "21211",
      },
      employmentType: "Short or Long Term",
      resume: {
        name: 'resume.pdf'
      },
      govId: {
        name: 'government-id.pdf'
      }
    },
  },
};
